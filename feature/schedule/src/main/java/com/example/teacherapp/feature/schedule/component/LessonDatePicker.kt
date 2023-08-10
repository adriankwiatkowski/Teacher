package com.example.teacherapp.feature.schedule.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.ui.component.picker.TeacherDatePicker
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate

@Composable
internal fun LessonDatePicker(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    Column {
        Text(
            text = TimeUtils.getDisplayNameOfDayOfWeek(date),
            style = MaterialTheme.typography.labelMedium,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Data:")
            Text(text = TimeUtils.format(date))
            TeacherDatePicker(
                date = date,
                onDateSelected = onDateSelected,
                label = { Text("Wybierz datę") },
            )
        }
    }
}

@Preview
@Composable
private fun LessonDatePickerPreview() {
    TeacherAppTheme {
        Surface {
            var date by remember { mutableStateOf(TimeUtils.currentDate()) }

            LessonDatePicker(
                date = date,
                onDateSelected = { date = it },
            )
        }
    }
}