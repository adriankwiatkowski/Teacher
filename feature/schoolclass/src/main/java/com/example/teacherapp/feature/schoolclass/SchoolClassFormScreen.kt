package com.example.teacherapp.feature.schoolclass

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.form.FormStatusContent
import com.example.teacherapp.core.ui.component.form.FormTextField
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.schoolclass.components.SchoolYearInput
import com.example.teacherapp.feature.schoolclass.data.SchoolClassFormProvider
import com.example.teacherapp.feature.schoolclass.previewparam.SchoolYearsPreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolClassFormScreen(
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    formStatus: FormStatus,
    isSubmitEnabled: Boolean,
    onAddSchoolYear: () -> Unit,
    onAddSchoolClass: () -> Unit,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TeacherTopBar(
                title = "Stwórz nową klasę",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        FormStatusContent(
            formStatus = formStatus,
            savingText = "Zapisywanie klasy...",
        ) {
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
                isSubmitEnabled = isSubmitEnabled,
                onSubmit = onAddSchoolClass,
                onAddSchoolYear = onAddSchoolYear,
            )
        }
    }
}

@Composable
private fun MainContent(
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    isSubmitEnabled: Boolean,
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
            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubmit,
                enabled = isSubmitEnabled,
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
    FormTextField(
        modifier = modifier,
        inputField = schoolClassName,
        onValueChange = onSchoolClassNameChange,
        label = "Nazwa klasy",
        prefix = "(Klasa) ",
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
                schoolClassName = form.schoolClassName,
                onSchoolClassNameChange = {},
                schoolYears = schoolYears,
                schoolYear = form.schoolYear,
                onSchoolYearChange = {},
                formStatus = form.status,
                isSubmitEnabled = form.isSubmitEnabled,
                onAddSchoolYear = {},
                onAddSchoolClass = {},
                showNavigationIcon = true,
                onNavBack = {},
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}