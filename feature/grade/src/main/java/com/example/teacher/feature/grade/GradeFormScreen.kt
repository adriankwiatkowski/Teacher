package com.example.teacher.feature.grade

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.GradeTemplateInfo
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.grade.data.GradeFormProvider
import com.example.teacher.feature.grade.data.GradeFormUiState
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
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    isEditMode: Boolean,
    isDeleted: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

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
                    submitText = if (isEditMode) {
                        stringResource(R.string.grade_edit_grade)
                    } else {
                        stringResource(R.string.grade_add_grade)
                    },
                    isSubmitEnabled = isSubmitEnabled,
                    onSubmit = onSubmit,
                )
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
    submitText: String,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        Header(
            student = uiState.student,
            gradeInfo = uiState.gradeTemplateInfo,
            initialGrade = initialGrade,
            inputGrade = inputGrade,
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

@Composable
private fun Header(
    student: BasicStudent,
    gradeInfo: GradeTemplateInfo,
    initialGrade: BigDecimal?,
    inputGrade: BigDecimal?,
    modifier: Modifier = Modifier,
) {
    val gradeName = gradeInfo.gradeName
    val gradeTermName = if (gradeInfo.isFirstTerm) {
        gradeInfo.lesson.schoolClass.schoolYear.firstTerm.name
    } else {
        gradeInfo.lesson.schoolClass.schoolYear.secondTerm.name
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
            Text(
                modifier = modifier,
                text = student.fullName,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(stringResource(R.string.grades_grade_with_term, gradeName, gradeTermName))
            Text(stringResource(R.string.grade_grade_weight, gradeInfo.gradeWeight))
            if (initialGrade != null) {
                Text(stringResource(R.string.grade_current_grade, toGradeWithLiteral(initialGrade)))
            }

            // Don't show new grade if it's same as old one.
            if (initialGrade == null || (initialGrade != inputGrade)) {
                Text(stringResource(R.string.grade_new_grade, toGradeWithLiteral(inputGrade)))
            }
        }
    }
}

@Composable
private fun GradeInputs(
    onGradeChange: (grade: BigDecimal?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GradeInputs(
            grades = listOf(DecimalUtils.One),
            onGradeClick = onGradeChange,
        )
        GradeInputs(
            grades = listOf(DecimalUtils.TwoMinus, DecimalUtils.Two, DecimalUtils.TwoPlus),
            onGradeClick = onGradeChange,
        )
        GradeInputs(
            grades = listOf(DecimalUtils.ThreeMinus, DecimalUtils.Three, DecimalUtils.ThreePlus),
            onGradeClick = onGradeChange,
        )
        GradeInputs(
            grades = listOf(DecimalUtils.FourMinus, DecimalUtils.Four, DecimalUtils.FourPlus),
            onGradeClick = onGradeChange,
        )
        GradeInputs(
            grades = listOf(DecimalUtils.FiveMinus, DecimalUtils.Five, DecimalUtils.FivePlus),
            onGradeClick = onGradeChange,
        )
        GradeInputs(
            grades = listOf(DecimalUtils.SixMinus, DecimalUtils.Six),
            onGradeClick = onGradeChange,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GradeInputs(
    grades: List<BigDecimal>,
    onGradeClick: (grade: BigDecimal?) -> Unit,
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
        for (grade in grades) {
            GradeInput(grade = DecimalUtils.toGrade(grade), onClick = { onGradeClick(grade) })
        }
    }
}

@Composable
private fun GradeInput(
    grade: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TeacherButton(modifier = modifier, label = grade, onClick = onClick)
}

@Composable
private fun toGradeWithLiteral(grade: BigDecimal?): String {
    return if (grade != null) {
        stringResource(
            R.string.grade_grade_with_literal,
            DecimalUtils.toGrade(grade),
            DecimalUtils.toLiteral(grade),
        )
    } else {
        stringResource(R.string.grade_no_grade)
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

            GradeFormScreen(
                uiStateResult = Result.Success(uiState),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                formStatus = form.status,
                initialGrade = null,
                inputGrade = form.grade.value,
                onGradeChange = { form = form.copy(grade = GradeFormProvider.validateGrade(it)) },
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = {},
                isEditMode = false,
                isDeleted = false,
                onDeleteClick = {},
            )
        }
    }
}