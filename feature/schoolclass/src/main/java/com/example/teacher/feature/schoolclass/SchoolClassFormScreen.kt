package com.example.teacher.feature.schoolclass

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.paramprovider.SchoolYearsPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schoolclass.components.SchoolYearInput
import com.example.teacher.feature.schoolclass.data.SchoolClassFormProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolClassFormScreen(
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    isEditMode: Boolean,
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    formStatus: FormStatus,
    isSubmitEnabled: Boolean,
    onEditSchoolYear: () -> Unit,
    onAddSchoolYear: () -> Unit,
    onAddSchoolClass: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = if (isEditMode) {
                    stringResource(R.string.school_class_form_edit_title)
                } else {
                    stringResource(R.string.school_class_form_title)
                },
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        FormStatusContent(
            formStatus = formStatus,
            savingText = stringResource(R.string.school_class_saving_school_class),
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
                submitText = if (isEditMode) {
                    stringResource(R.string.school_class_edit_school_class)
                } else {
                    stringResource(R.string.school_class_add_school_class)
                },
                isSubmitEnabled = isSubmitEnabled,
                onSubmit = onAddSchoolClass,
                onAddSchoolYear = onAddSchoolYear,
                onEditSchoolYear = onEditSchoolYear,
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
    submitText: String,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    onAddSchoolYear: () -> Unit,
    onEditSchoolYear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.small),
    ) {
        ClassNameInput(
            modifier = Modifier.fillMaxWidth(),
            schoolClassName = schoolClassName,
            onSchoolClassNameChange = onSchoolClassNameChange,
        )

        Spacer(Modifier.padding(MaterialTheme.spacing.small))

        Card {
            SchoolYearInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                schoolYears = schoolYears,
                schoolYear = schoolYear,
                onSchoolYearChange = onSchoolYearChange,
                onAddSchoolYear = onAddSchoolYear,
                onEditSchoolYear = onEditSchoolYear,
            )
        }

        Spacer(Modifier.padding(MaterialTheme.spacing.small))

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            label = submitText,
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        )
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
        label = stringResource(R.string.school_class_name_label),
        prefix = stringResource(R.string.school_class_prefix),
    )
}

@Preview
@Composable
private fun SchoolClassFormScreenPreview(
    @PreviewParameter(SchoolYearsPreviewParameterProvider::class) schoolYears: List<SchoolYear>,
) {
    TeacherTheme {
        Surface {
            val form = SchoolClassFormProvider.createDefaultForm()
            SchoolClassFormScreen(
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                isEditMode = true,
                schoolClassName = form.schoolClassName,
                onSchoolClassNameChange = {},
                schoolYears = schoolYears,
                schoolYear = form.schoolYear,
                onSchoolYearChange = {},
                formStatus = form.status,
                isSubmitEnabled = form.isSubmitEnabled,
                onAddSchoolYear = {},
                onAddSchoolClass = {},
                onEditSchoolYear = {},
            )
        }
    }
}