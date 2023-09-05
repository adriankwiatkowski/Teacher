package com.example.teacher.feature.schoolyear.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.component.picker.TeacherDatePicker
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schoolyear.R
import com.example.teacher.feature.schoolyear.data.InputDate
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
            label = stringResource(R.string.term_name_label),
            prefix = namePrefix,
        )

        TermDatePicker(
            label = stringResource(R.string.term_start_date_label),
            date = startDate.date,
            dateString = startDate.dateString,
            onDateSelected = onStartDateSelected,
        )
        TermDatePicker(
            label = stringResource(R.string.term_end_date_label),
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
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = label)
        Text(text = dateString)
        TeacherDatePicker(
            date = date,
            onDateSelected = onDateSelected,
            label = stringResource(R.string.pick_date),
        )
    }
}

@Preview
@Composable
private fun TermFormPreview() {
    TeacherTheme {
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
                startDate = InputDate(startDate, TimeUtils.format(startDate)),
                onStartDateSelected = {},
                endDate = InputDate(endDate, TimeUtils.format(endDate)),
                onEndDateSelected = {},
            )
        }
    }
}