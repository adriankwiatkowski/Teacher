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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.form.FormStatusContent
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.provider.ActionItemProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.ui.screens.grade.data.GradeFormProvider
import com.example.teacherapp.ui.screens.grade.data.GradeFormUiState
import com.example.teacherapp.ui.screens.paramproviders.GradeFormUiStatePreviewParameterProvider
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeFormScreen(
    uiStateResult: Result<GradeFormUiState>,
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
        topBar = {
            TeacherTopBar(
                title = "Wystawienie oceny",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(ActionItemProvider.delete(onDeleteClick))
                } else {
                    emptyList()
                },
                scrollBehavior = scrollBehavior,
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
                    initialGrade = initialGrade,
                    inputGrade = inputGrade,
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
        StudentInfo(student = uiState.student)
        GradeInfo(
            gradeInfo = uiState.gradeTemplateInfo,
            initialGrade = initialGrade,
            inputGrade = inputGrade,
        )
        GradeInputs(onGradeChange = onGradeChange)

        TeacherButton(
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

        val rowArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)

        FlowRow(horizontalArrangement = rowArrangement) {
            GradeInput(grade = "2-", onClick = { onGradeChange(BigDecimal("1.75")) })
            GradeInput(grade = "2", onClick = { onGradeChange(BigDecimal("2.00")) })
            GradeInput(grade = "2+", onClick = { onGradeChange(BigDecimal("2.50")) })
        }
        FlowRow(horizontalArrangement = rowArrangement) {
            GradeInput(grade = "3-", onClick = { onGradeChange(BigDecimal("2.75")) })
            GradeInput(grade = "3", onClick = { onGradeChange(BigDecimal("3.00")) })
            GradeInput(grade = "3+", onClick = { onGradeChange(BigDecimal("3.50")) })
        }
        FlowRow(horizontalArrangement = rowArrangement) {
            GradeInput(grade = "4-", onClick = { onGradeChange(BigDecimal("3.75")) })
            GradeInput(grade = "4", onClick = { onGradeChange(BigDecimal("4.00")) })
            GradeInput(grade = "4+", onClick = { onGradeChange(BigDecimal("4.50")) })
        }
        FlowRow(horizontalArrangement = rowArrangement) {
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
    TeacherButton(modifier = modifier, onClick = onClick) {
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