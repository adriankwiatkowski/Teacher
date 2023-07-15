package com.example.teacherapp.ui.screens.schoolclass

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.transformation.PrefixTransformation
import com.example.teacherapp.ui.screens.paramproviders.SchoolYearsPreviewParameterProvider
import com.example.teacherapp.ui.screens.schoolclass.components.SchoolYearInput
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassFormProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun SchoolClassFormScreen(
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    status: FormStatus,
    canSubmit: Boolean,
    onAddSchoolYear: () -> Unit,
    onAddSchoolClass: () -> Unit,
    onSchoolClassAdded: () -> Unit,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(status) {
        if (status is FormStatus.Success) {
            onSchoolClassAdded()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TeacherTopBar(
                title = "Stwórz nową klasę",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
            )
        }
    ) { innerPadding ->
        MainContent(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            schoolClassName = schoolClassName,
            onSchoolClassNameChange = onSchoolClassNameChange,
            schoolYears = schoolYears,
            schoolYear = schoolYear,
            onSchoolYearChange = onSchoolYearChange,
            canSubmit = canSubmit,
            onSubmit = onAddSchoolClass,
            onAddSchoolYear = onAddSchoolYear,
        )
    }
}

@Composable
private fun MainContent(
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    canSubmit: Boolean,
    onSubmit: () -> Unit,
    onAddSchoolYear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        item {
            ClassNameInput(
                modifier = Modifier.fillMaxWidth(),
                schoolClassName = schoolClassName,
                onSchoolClassNameChange = onSchoolClassNameChange,
            )
        }

        item {
            Card {
                SchoolYearInput(
                    modifier = Modifier.fillMaxWidth(),
                    schoolYears = schoolYears,
                    schoolYear = schoolYear,
                    onSchoolYearChange = onSchoolYearChange,
                    onAddSchoolYear = onAddSchoolYear,
                )
            }
        }

        item {
            TeacherOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubmit,
                enabled = canSubmit,
            ) {
                Text(text = "Dodaj klasę")
            }
        }
    }
}

@Composable
private fun ClassNameInput(
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormOutlinedTextField(
        modifier = modifier,
        inputField = schoolClassName,
        onValueChange = onSchoolClassNameChange,
        label = "Nazwa klasy",
        visualTransformation = PrefixTransformation("(Klasa) ")
    )
}

@Preview
@Composable
private fun SchoolClassFormScreenPreview(
    @PreviewParameter(SchoolYearsPreviewParameterProvider::class) schoolYears: List<SchoolYear>,
) {
    TeacherAppTheme {
        Surface {
            val form = SchoolClassFormProvider.createDefaultForm()
            SchoolClassFormScreen(
                modifier = Modifier.fillMaxSize(),
                schoolClassName = form.schoolClassName,
                onSchoolClassNameChange = {},
                schoolYears = schoolYears,
                schoolYear = form.schoolYear,
                onSchoolYearChange = {},
                status = form.status,
                canSubmit = form.canSubmit,
                onAddSchoolYear = {},
                onAddSchoolClass = {},
                onSchoolClassAdded = {},
                showNavigationIcon = true,
                onNavBack = {},
            )
        }
    }
}