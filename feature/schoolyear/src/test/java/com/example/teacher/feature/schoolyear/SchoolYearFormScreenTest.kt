package com.example.teacher.feature.schoolyear

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolyear.SchoolYearRepository
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.feature.schoolyear.data.SchoolYearFormViewModel
import com.example.teacher.feature.schoolyear.nav.SchoolYearNavigation
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
class SchoolYearFormScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var schoolClassDataSource: SchoolClassDataSource

    @Inject
    lateinit var schoolYearDataSource: SchoolYearDataSource

    @Inject
    lateinit var schoolYearRepository: SchoolYearRepository

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun createWorks() {
        val listMatcher = hasScrollToNodeAction()
        val nameMatcher = hasText(rule.activity.getString(R.string.school_year_name) + "*")
        val termNameMatcher =
            hasText(rule.activity.getString(R.string.school_year_term_name_label) + "*")
        val submitMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.school_year_add_school_year))
        val viewModel = ViewModelProvider(rule.activity)[SchoolYearFormViewModel::class.java]
        rule.setContent { SchoolYearFormScreen(viewModel) }

        rule.onRoot().printToLog("School Year Form")

        rule.onNode(nameMatcher).performTextClearance()
        rule.onNode(nameMatcher).performTextInput("Year 2023/2024")
        rule.onAllNodes(termNameMatcher)[0].performTextClearance()
        rule.onAllNodes(termNameMatcher)[0].performTextInput("1")
        rule.onAllNodes(termNameMatcher)[1].performTextClearance()
        rule.onAllNodes(termNameMatcher)[1].performTextInput("2")

        rule.onNode(listMatcher).performScrollToNode(submitMatcher)
        rule.onNode(submitMatcher).assertIsEnabled().performClick()
        viewModel.onDeleteSchoolYear()
    }

    @Test
    fun updateWorks() = runTest {
        givenSchoolClass(schoolClassDataSource, schoolYearDataSource, testSchoolClass())
        val listMatcher = hasScrollToNodeAction()
        val submitMatcher =
            hasClickAction() and hasText(rule.activity.getString(R.string.school_year_edit_school_year))
        val savedStateHandle = SavedStateHandle(mapOf(SchoolYearNavigation.schoolYearIdArg to 1L))
        val viewModel = SchoolYearFormViewModel(schoolYearRepository, savedStateHandle)
        rule.setContent { SchoolYearFormScreen(viewModel, isEditMode = true) }

        rule.waitUntil { viewModel.schoolYearResult.value is Result.Success }
        rule.onRoot().printToLog("School Year Form")

        rule.onNode(listMatcher).performScrollToNode(submitMatcher)
        rule.onNode(submitMatcher).assertIsEnabled().performClick()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun SchoolYearFormScreen(
        viewModel: SchoolYearFormViewModel,
        isEditMode: Boolean = false,
    ) {
        val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()
        val form by viewModel.form.collectAsStateWithLifecycle()
        SchoolYearFormScreen(
            snackbarHostState = remember { SnackbarHostState() },
            termForms = form.termForms,
            showNavigationIcon = false,
            onNavBack = {},
            isEditMode = isEditMode,
            isDeleted = isDeleted,
            schoolYearName = form.schoolYearName,
            onSchoolYearNameChange = viewModel::onSchoolYearNameChange,
            onTermNameChange = viewModel::onTermNameChange,
            onStartDateChange = viewModel::onStartDateChange,
            onEndDateChange = viewModel::onEndDateChange,
            status = form.status,
            isSubmitEnabled = form.isSubmitEnabled,
            onAddSchoolYear = viewModel::onAddSchoolYear,
            onDeleteSchoolYear = {},
        )
    }
}