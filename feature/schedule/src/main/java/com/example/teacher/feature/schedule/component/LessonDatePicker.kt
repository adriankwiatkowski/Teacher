package com.example.teacher.feature.schedule.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.ui.component.picker.TeacherDatePicker
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schedule.R
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
            Text(text = stringResource(R.string.schedule_date))
            Text(text = TimeUtils.format(date))
            TeacherDatePicker(
                date = date,
                onDateSelected = onDateSelected,
                label = stringResource(R.string.schedule_pick_date),
            )
        }
    }
}

@Preview
@Composable
private fun LessonDatePickerPreview() {
    TeacherTheme {
        Surface {
            var date by remember { mutableStateOf(TimeUtils.currentDate()) }

            LessonDatePicker(
                date = date,
                onDateSelected = { date = it },
            )
        }
    }
}