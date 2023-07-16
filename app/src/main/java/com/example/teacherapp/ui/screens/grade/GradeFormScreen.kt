package com.example.teacherapp.ui.screens.grade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
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
        Text(text = "Main Content")

        TeacherOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        ) {
            Text(text = submitText)
        }
    }
}

private fun gradeToName(grade: BigDecimal?): String = grade?.toString() ?: "brak oceny"

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