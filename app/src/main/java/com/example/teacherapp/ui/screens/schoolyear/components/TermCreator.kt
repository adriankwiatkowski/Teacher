package com.example.teacherapp.ui.screens.schoolyear.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teacherapp.data.models.input.InputDate
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.pickers.DatePicker
import com.example.teacherapp.ui.components.transformation.PrefixTransformation
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.utils.format
import java.time.LocalDate

@Composable
fun TermCreator(
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FormOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            inputField = nameInput,
            onValueChange = onNameChange,
            label = "Nazwa semestru",
            visualTransformation = PrefixTransformation(namePrefix)
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
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)
        Text(text = dateString)
        DatePicker(
            date = date,
            onDateSelected = onDateSelected,
            label = {
                Text(modifier = Modifier.padding(8.dp), text = "Wybierz datę")
            },
        )
    }
}

@Preview
@Composable
private fun TermScreenPreview() {
    TeacherAppTheme {
        Surface {
            val startDate = LocalDate.now()
            val endDate = startDate.plusDays(1L)

            val startYear = startDate.year
            val endYear = startYear + 1
            val name = "Rok $startYear/$endYear I"

            TermCreator(
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