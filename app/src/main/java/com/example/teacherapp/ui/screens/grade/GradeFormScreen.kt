package com.example.teacherapp.ui.screens.grade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.components.form.FormStatusContent
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.resource.ResultContent
import com.example.teacherapp.ui.screens.grade.data.GradeFormProvider
import com.example.teacherapp.ui.screens.grade.data.GradeFormUiState
import com.example.teacherapp.ui.screens.paramproviders.GradeFormUiStatePreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing
import java.math.BigDecimal

@Composable
fun GradeFormScreen(
    uiStateResult: Result<GradeFormUiState>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    formStatus: FormStatus,
    grade: BigDecimal?,
    onGradeChange: (grade: BigDecimal?) -> Unit,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    isEditMode: Boolean,
    isDeleted: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TeacherTopBar(
                title = "Wystawienie oceny",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(ActionMenuItemProvider.delete(onDeleteClick))
                } else {
                    emptyList()
                },
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            result = uiStateResult,
            isDeleted = isDeleted,
            deletedMessage = "Usunięto ocenę",
        ) { uiState ->
            FormStatusContent(
                formStatus = formStatus,
                savingText = "Zapisywanie oceny...",
            ) {
                MainContent(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    uiState = uiState,
                    grade = grade,
                    onGradeChange = onGradeChange,
                    submitText = if (isEditMode) "Edytuj ocenę" else "Dodaj ocenę",
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
    grade: BigDecimal?,
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
        StudentInfo(student = uiState.student)
        GradeInfo(
            gradeInfo = uiState.gradeTemplateInfo,
            initialGrade = grade,
            inputGrade = grade,
        )
        GradeInputs(onGradeChange = onGradeChange)

        TeacherOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        ) {
            Text(text = submitText)
        }
    }
}

@Composable
private fun StudentInfo(
    student: BasicStudent,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Text(text = student.fullName)
    }
}

@Composable
private fun GradeInfo(
    gradeInfo: GradeTemplateInfo,
    initialGrade: BigDecimal?,
    inputGrade: BigDecimal?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column {
            Text(gradeInfo.gradeName)
            Text("Waga ${gradeInfo.gradeWeight}")
            if (initialGrade != null) {
                Text("Obecna ocena $initialGrade")
            } else {
                Text("Brak oceny")
            }
            Text("Nowa ocena: ${inputGrade?.toString() ?: "brak oceny"}")
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
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
        GradeInput(grade = "1", onClick = { onGradeChange(BigDecimal("1.00")) })

        FlowRow {
            GradeInput(grade = "2-", onClick = { onGradeChange(BigDecimal("1.75")) })
            GradeInput(grade = "2", onClick = { onGradeChange(BigDecimal("2.00")) })
            GradeInput(grade = "2+", onClick = { onGradeChange(BigDecimal("2.50")) })
        }
        FlowRow {
            GradeInput(grade = "3-", onClick = { onGradeChange(BigDecimal("2.75")) })
            GradeInput(grade = "3", onClick = { onGradeChange(BigDecimal("3.00")) })
            GradeInput(grade = "3+", onClick = { onGradeChange(BigDecimal("3.50")) })
        }
        FlowRow {
            GradeInput(grade = "4-", onClick = { onGradeChange(BigDecimal("3.75")) })
            GradeInput(grade = "4", onClick = { onGradeChange(BigDecimal("4.00")) })
            GradeInput(grade = "4+", onClick = { onGradeChange(BigDecimal("4.50")) })
        }
        FlowRow {
            GradeInput(grade = "5-", onClick = { onGradeChange(BigDecimal("4.75")) })
            GradeInput(grade = "5", onClick = { onGradeChange(BigDecimal("5.00")) })
            GradeInput(grade = "5+", onClick = { onGradeChange(BigDecimal("5.50")) })
        }

        GradeInput(grade = "6", onClick = { onGradeChange(BigDecimal("6.00")) })
    }
}

@Composable
private fun GradeInput(
    grade: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(modifier = modifier, onClick = onClick) {
        Text(grade)
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
    TeacherAppTheme {
        Surface {
            var form by remember { mutableStateOf(GradeFormProvider.createDefaultForm()) }

            GradeFormScreen(
                uiStateResult = Result.Success(uiState),
                showNavigationIcon = true,
                onNavBack = {},
                formStatus = form.status,
                grade = form.grade.value,
                onGradeChange = { form = form.copy(GradeFormProvider.validateGrade(it)) },
                isSubmitEnabled = form.isSubmitEnabled,
                onSubmit = {},
                isEditMode = false,
                isDeleted = false,
                onDeleteClick = {},
            )
        }
    }
}