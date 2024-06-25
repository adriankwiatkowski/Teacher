package com.example.teacher.feature.schoolclass

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.schoolclass.data.SchoolClassFormViewModel
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SchoolClassFormScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Inject
    lateinit var schoolClassRepository: SchoolClassRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun emptySchoolYears() {
        val viewModel = ViewModelProvider(rule.activity)[SchoolClassFormViewModel::class.java]
        rule.setContent { SchoolClassFormScreen(viewModel) }

        rule.waitUntil { viewModel.schoolClassResult.value is Result.Success }
        rule.onRoot().printToLog("School Class Form")

        rule.onNodeWithText(rule.activity.getString(R.string.school_class_no_school_year))
            .assertExists()
    }

    @Test
    fun createWorks() = runTest {
        val schoolClass = testSchoolClass()
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, schoolClass)
        val nameMatcher =
            hasText(rule.activity.getString(R.string.school_class_name_label), substring = true)
        val yearMatcher =
            hasText(rule.activity.getString(R.string.school_class_school_year), substring = true)
        val yearItemMatcher =
            isNotFocused() and hasText(schoolClass.schoolYear.name, substring = true)
        val submitMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.school_class_add_school_class))
        val viewModel = ViewModelProvider(rule.activity)[SchoolClassFormViewModel::class.java]
        rule.setContent { SchoolClassFormScreen(viewModel) }

        rule.waitUntil { viewModel.schoolClassResult.value is Result.Success }
        rule.onRoot().printToLog("School Class Form")

        rule.onNode(nameMatcher).performTextInput("1A")
        rule.onNode(yearMatcher).performClick()
        rule.onNode(yearItemMatcher).performClick()

        rule.onNode(submitMatcher).assertIsEnabled().performClick()
    }

    @Test
    fun updateWorks() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        val submitMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.school_class_edit_school_class))
        val savedStateHandle = SavedStateHandle(mapOf(SchoolClassNavigation.schoolClassIdArg to 1L))
        val viewModel = SchoolClassFormViewModel(schoolClassRepository, savedStateHandle)
        rule.setContent { SchoolClassFormScreen(viewModel, isEditMode = true) }

        rule.waitUntil { viewModel.schoolClassResult.value is Result.Success }
        rule.onRoot().printToLog("School Class Form")

        rule.onNode(submitMatcher).assertIsEnabled().performClick()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun SchoolClassFormScreen(
        viewModel: SchoolClassFormViewModel,
        isEditMode: Boolean = false,
    ) {
        val schoolYears by viewModel.schoolYears.collectAsStateWithLifecycle()
        val form by viewModel.form.collectAsStateWithLifecycle()
        SchoolClassFormScreen(
            snackbarHostState = remember { SnackbarHostState() },
            showNavigationIcon = false,
            onNavBack = {},
            isEditMode = isEditMode,
            schoolClassName = form.schoolClassName,
            onSchoolClassNameChange = viewModel::onSchoolClassNameChange,
            schoolYears = schoolYears,
            schoolYear = form.schoolYear,
            onSchoolYearChange = viewModel::onSchoolYearChange,
            formStatus = form.status,
            isSubmitEnabled = form.isSubmitEnabled,
            onAddSchoolYear = {},
            onAddSchoolClass = viewModel::onSubmit,
            onEditSchoolYear = {},
        )
    }
}