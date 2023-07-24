package com.example.teacherapp.feature.schoolyear.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.utils.format
import com.example.teacherapp.core.ui.component.form.FormTextField
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.model.InputField
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.schoolyear.data.InputDate
import java.time.LocalDate

@Composable
internal fun TermForm(
    namePrefix: String,
    nameInput: InputField<String>,
    onNameChange: (name: String) -> Unit,
    startDate: InputDate,
    onStartDateSelected: (LocalDate) -> Unit,
    endDate: InputDate,
    onEndDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        FormTextField(
            modifier = Modifier.fillMaxWidth(),
            inputField = nameInput,
            onValueChange = onNameChange,
            label = "Nazwa semestru",
            prefix = namePrefix,
        )

        TermDatePicker(
            label = "Data rozpoczęcia semestru",
            date = startDate.date,
            dateString = startDate.dateString,
            onDateSelected = onStartDateSelected,
        )
        TermDatePicker(
            label = "Data zakończenia semestru",
            date = endDate.date,
            dateString = endDate.dateString,
            onDateSelected = onEndDateSelected,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TermDatePicker(
    label: String,
    date: LocalDate,
    dateString: String,
    onDateSelected: (LocalDate) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)
        Text(text = dateString)
        TeacherDatePicker(
            date = date,
            onDateSelected = onDateSelected,
            label = {
                Text(
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                    text = "Wybierz datę",
                )
            },
        )
    }
}

@Preview
@Composable
private fun TermFormPreview() {
    TeacherAppTheme {
        Surface {
            val startDate = LocalDate.now()
            val endDate = startDate.plusDays(1L)

            val startYear = startDate.year
            val endYear = startYear + 1
            val name = "Rok $startYear/$endYear I"

            TermForm(
                namePrefix = "",
                nameInput = InputField(name),
                onNameChange = {},
                startDate = InputDate(startDate, startDate.format()),
                onStartDateSelected = {},
                endDate = InputDate(endDate, endDate.format()),
                onEndDateSelected = {},
            )
        }
    }
}