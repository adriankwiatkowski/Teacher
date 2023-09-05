package com.example.teacher.feature.schoolyear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schoolyear.components.TermForm
import com.example.teacher.feature.schoolyear.data.InputDate
import com.example.teacher.feature.schoolyear.data.SchoolYearFormProvider
import com.example.teacher.feature.schoolyear.data.TermForm
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolYearFormScreen(
    snackbarHostState: SnackbarHostState,
    termForms: List<TermForm>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    schoolYearName: InputField<String>,
    onSchoolYearNameChange: (String) -> Unit,
    onTermNameChange: (index: Int, name: String) -> Unit,
    onStartDateChange: (index: Int, date: LocalDate) -> Unit,
    onEndDateChange: (index: Int, date: LocalDate) -> Unit,
    status: FormStatus,
    isSubmitEnabled: Boolean,
    onAddSchoolYear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = stringResource(R.string.school_year_form_title),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        FormStatusContent(
            modifier = Modifier.padding(innerPadding),
            formStatus = status,
            savingText = stringResource(R.string.saving_school_year),
        ) {
            MainContent(
                termForms = termForms,
                schoolYearName = schoolYearName,
                onSchoolYearNameChange = onSchoolYearNameChange,
                onTermNameChange = onTermNameChange,
                onStartDateChange = onStartDateChange,
                onEndDateChange = onEndDateChange,
                isSubmitEnabled = isSubmitEnabled,
                onSubmit = onAddSchoolYear
            )
        }
    }
}

@Composable
private fun MainContent(
    termForms: List<TermForm>,
    schoolYearName: InputField<String>,
    onSchoolYearNameChange: (String) -> Unit,
    onTermNameChange: (index: Int, name: String) -> Unit,
    onStartDateChange: (index: Int, date: LocalDate) -> Unit,
    onEndDateChange: (index: Int, date: LocalDate) -> Unit,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        item {
            SchoolYearNameInput(
                schoolYearNameInput = schoolYearName,
                onSchoolYearNameChange = onSchoolYearNameChange,
            )
        }

        itemsIndexed(
            termForms,
            key = { _, item -> item.formId },
        ) { index, termForm ->
            TeamFormItem(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.term_with_index, index + 1),
                namePrefix = "(${schoolYearName.value}) ",
                nameInput = termForm.name,
                onNameChange = { onTermNameChange(index, it) },
                startDate = termForm.startDate,
                onStartDateSelected = { onStartDateChange(index, it) },
                endDate = termForm.endDate,
                onEndDateSelected = { onEndDateChange(index, it) },
            )
        }

        item {
            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.add_school_year),
                onClick = onSubmit,
                enabled = isSubmitEnabled,
            )
        }
    }
}


@Composable
private fun SchoolYearNameInput(
    schoolYearNameInput: InputField<String>,
    onSchoolYearNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val label = stringResource(R.string.school_year_name)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = label,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        FormTextField(
            modifier = Modifier.fillMaxWidth(),
            inputField = schoolYearNameInput,
            onValueChange = onSchoolYearNameChange,
            label = label,
        )
    }
}

@Composable
private fun TeamFormItem(
    title: String,
    namePrefix: String,
    nameInput: InputField<String>,
    onNameChange: (name: String) -> Unit,
    startDate: InputDate,
    onStartDateSelected: (LocalDate) -> Unit,
    endDate: InputDate,
    onEndDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )

            TermForm(
                namePrefix = namePrefix,
                nameInput = nameInput,
                onNameChange = onNameChange,
                startDate = startDate,
                onStartDateSelected = onStartDateSelected,
                endDate = endDate,
                onEndDateSelected = onEndDateSelected,
            )
        }
    }
}

@Preview
@Composable
private fun SchoolYearFormScreenPreview() {
    TeacherTheme {
        Surface {
            val form = SchoolYearFormProvider.createDefaultForm()

            SchoolYearFormScreen(
                snackbarHostState = remember { SnackbarHostState() },
                termForms = form.termForms,
                showNavigationIcon = true,
                onNavBack = {},
                schoolYearName = form.schoolYearName,
                onSchoolYearNameChange = {},
                onTermNameChange = { _, _ -> },
                onStartDateChange = { _, _ -> },
                onEndDateChange = { _, _ -> },
                status = form.status,
                isSubmitEnabled = form.isValid,
                onAddSchoolYear = {},
            )
        }
    }
}