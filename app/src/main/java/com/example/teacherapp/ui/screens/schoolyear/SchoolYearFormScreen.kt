package com.example.teacherapp.ui.screens.schoolyear

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputDate
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.form.FormStatusContent
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.screens.schoolyear.components.TermForm
import com.example.teacherapp.ui.screens.schoolyear.data.SchoolYearFormProvider
import com.example.teacherapp.ui.screens.schoolyear.data.TermForm
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing
import java.time.LocalDate

@Composable
fun SchoolYearFormScreen(
    termForms: List<TermForm>,
    schoolYearName: InputField<String>,
    onSchoolYearNameChange: (String) -> Unit,
    onTermNameChange: (index: Int, name: String) -> Unit,
    onStartDateChange: (index: Int, date: LocalDate) -> Unit,
    onEndDateChange: (index: Int, date: LocalDate) -> Unit,
    status: FormStatus,
    isValid: Boolean,
    onAddSchoolYear: () -> Unit,
    onSchoolYearAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(status) {
        if (status == FormStatus.Success) {
            onSchoolYearAdd()
        }
    }

    val isSubmitEnabled = isValid && status != FormStatus.Saving

    FormStatusContent(
        modifier = modifier,
        formStatus = status,
        savingText = "Dodawanie roku szkolnego...",
    ) {
        Column(modifier = modifier) {
            LazyColumn(
                modifier = modifier,
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
                        title = "Semestr ${index + 1}",
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
                    TeacherOutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onAddSchoolYear,
                        enabled = isSubmitEnabled,
                    ) {
                        Text(text = "Dodaj rok szkolny")
                    }
                }
            }
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
        val label = "Nazwa roku szkolnego"

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = label,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
        )
        FormOutlinedTextField(
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
                style = MaterialTheme.typography.h5,
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
    TeacherAppTheme {
        Surface {
            val form = SchoolYearFormProvider.createDefaultForm()

            SchoolYearFormScreen(
                modifier = Modifier.fillMaxSize(),
                termForms = form.termForms,
                schoolYearName = form.schoolYearName,
                onSchoolYearNameChange = {},
                onTermNameChange = { _, _ -> },
                onStartDateChange = { _, _ -> },
                onEndDateChange = { _, _ -> },
                status = form.status,
                isValid = form.isValid,
                onAddSchoolYear = {},
                onSchoolYearAdd = {},
            )
        }
    }
}