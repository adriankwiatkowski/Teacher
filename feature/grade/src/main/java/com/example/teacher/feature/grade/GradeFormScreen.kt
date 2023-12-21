package com.example.teacher.feature.grade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.grade.component.GradeFormHeader
import com.example.teacher.feature.grade.component.GradeInputs
import com.example.teacher.feature.grade.component.GradeScoreForm
import com.example.teacher.feature.grade.data.GradeFormProvider
import com.example.teacher.feature.grade.data.GradeFormUiState
import com.example.teacher.feature.grade.data.GradeScoreData
import com.example.teacher.feature.grade.data.GradeScoreDataProvider
import com.example.teacher.feature.grade.paramprovider.GradeFormUiStatePreviewParameterProvider
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GradeFormScreen(
    uiStateResult: Result<GradeFormUiState>,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    formStatus: FormStatus,
    initialGrade: BigDecimal?,
    inputGrade: BigDecimal?,
    onGradeChange: (grade: BigDecimal?) -> Unit,
    gradeScoreData: GradeScoreData,
    onGradeScoreThresholdChange: (grade: BigDecimal, newMinThreshold: Int) -> Unit,
    onMaxScoreChange: (maxScore: String?) -> Unit,
    onStudentScoreChange: (studentScore: String?) -> Unit,
    onSaveGradeScore: () -> Unit,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    isEditMode: Boolean,
    isDeleted: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = stringResource(R.string.grade_grade_form_title),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(TeacherActions.delete(onDeleteClick))
                } else {
                    emptyList()
                },
                scrollBehavior = scrollBehavior,
                closeIcon = true,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            result = uiStateResult,
            isDeleted = isDeleted,
            deletedMessage = stringResource(R.string.grade_grade_deleted),
        ) { uiState ->
            FormStatusContent(
                formStatus = formStatus,
                savingText = stringResource(R.string.grade_saving_grade),
            ) {
                MainContent(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    uiState = uiState,
                    initialGrade = initialGrade,
                    inputGrade = inputGrade,
                    onGradeChange = onGradeChange,
                    onShowGradeScoreForm = { showBottomSheet = true },
                    submitText = if (isEditMode) {
                        stringResource(R.string.grade_edit_grade)
                    } else {
                        stringResource(R.string.grade_add_grade)
                    },
                    isSubmitEnabled = isSubmitEnabled,
                    onSubmit = onSubmit,
                )

                if (showBottomSheet) {
                    ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                        GradeScoreForm(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(MaterialTheme.spacing.medium),
                            gradeScoreData = gradeScoreData,
                            onGradeScoreThresholdChange = onGradeScoreThresholdChange,
                            onMaxScoreChange = onMaxScoreChange,
                            onStudentScoreChange = onStudentScoreChange,
                            onSaveGradeScore = onSaveGradeScore,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainContent(
    uiState: GradeFormUiState,
    initialGrade: BigDecimal?,
    inputGrade: BigDecimal?,
    onGradeChange: (grade: BigDecimal?) -> Unit,
    onShowGradeScoreForm: () -> Unit,
    submitText: String,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        GradeFormHeader(
            student = uiState.student,
            gradeInfo = uiState.gradeTemplateInfo,
            initialGrade = initialGrade,
            inputGrade = inputGrade,
            onShowGradeScoreForm = onShowGradeScoreForm,
        )

        GradeInputs(onGradeChange = onGradeChange)

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            label = submitText,
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        )
    }
}

@Preview
@Composable
private fun GradeFormScreenPreview(
    @PreviewParameter(
        GradeFormUiStatePreviewParameterProvider::class,
        limit = 1,
    ) uiState: GradeFormUiState,
) {
    TeacherTheme {
        Surface {
            var form by remember { mutableStateOf(GradeFormProvider.createDefaultForm()) }
            var gradeScoreData by remember { mutableStateOf(GradeScoreDataProvider.createDefault()) }

            GradeFormScreen(
                uiStateResult = Result.Success(uiState),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                formStatus = form.status,
                initialGrade = null,
                inputGrade = form.grade.value,
                onGradeChange = { form = form.copy(grade = GradeFormProvider.validateGrade(it)) },
                gradeScoreData = gradeScoreData,
                onGradeScoreThresholdChange = { grade, minThreshold ->
                    gradeScoreData = GradeScoreDataProvider.validateGradeScores(
                        gradeScoreData = gradeScoreData,
                        grade = grade,
                        newMinThreshold = minThreshold,
                    )
                },
                onMaxScoreChange = { maxScore ->
                    gradeScoreData = gradeScoreData.copy(
                        maxScore = GradeScoreDataProvider.validateGradeScore(maxScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onStudentScoreChange = { studentScore ->
                    gradeScoreData = gradeScoreData.copy(
                        studentScore = GradeScoreDataProvider.validateGradeScore(studentScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onSaveGradeScore = {},
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = {},
                isEditMode = false,
                isDeleted = false,
                onDeleteClick = {},
            )
        }
    }
}