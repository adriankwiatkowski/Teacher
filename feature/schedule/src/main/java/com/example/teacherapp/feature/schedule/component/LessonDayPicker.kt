package com.example.teacherapp.feature.schedule.component

import androidx.compose.foundation.layout.Arrangement
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
import com.example.teacherapp.core.ui.component.picker.TeacherDayPicker
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.DayOfWeek

@Composable
internal fun LessonDayPicker(
    day: DayOfWeek,
    onDaySelected: (DayOfWeek) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Dzień: ${TimeUtils.getDisplayNameOfDayOfWeek(day)}")
        TeacherDayPicker(
            day = day,
            onDaySelected = onDaySelected,
            label = { Text("Wybierz dzień") },
        )
    }
}

@Preview
@Composable
private fun LessonDayPickerPreview() {
    TeacherAppTheme {
        Surface {
            var day by remember { mutableStateOf(TimeUtils.monday()) }

            LessonDayPicker(
                day = day,
                onDaySelected = { day = it },
            )
        }
    }
}