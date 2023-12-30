package com.example.teacher.feature.schoolclass

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.schoolclass.data.SchoolClassesViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SchoolClassesScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun empty() {
        val viewModel = ViewModelProvider(rule.activity)[SchoolClassesViewModel::class.java]
        rule.setContent { SchoolClassesScreen(viewModel) }
        rule.onRoot().printToLog("School Classes")

        rule.waitUntil { viewModel.schoolClassesResult.value is Result.Success }

        rule.onNodeWithText(rule.activity.getString(R.string.school_classes_empty))
            .assertExists()
    }

    @Test
    fun list() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass(name = "1A"))
        val viewModel = ViewModelProvider(rule.activity)[SchoolClassesViewModel::class.java]
        rule.setContent { SchoolClassesScreen(viewModel) }
        rule.onRoot().printToLog("School Classes")

        rule.waitUntil { viewModel.schoolClassesResult.value is Result.Success }

        rule.onNodeWithText(rule.activity.getString(R.string.school_class, "1A")).assertExists()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun SchoolClassesScreen(viewModel: SchoolClassesViewModel) {
        val schoolClassesResult by viewModel.schoolClassesResult.collectAsStateWithLifecycle()
        SchoolClassesScreen(
            schoolClassesResult = schoolClassesResult,
            snackbarHostState = remember { SnackbarHostState() },
            onAddSchoolClassClick = {},
            onClassClick = {},
        )
    }
}