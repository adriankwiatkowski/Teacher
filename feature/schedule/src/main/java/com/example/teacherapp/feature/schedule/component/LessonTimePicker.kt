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
import com.example.teacherapp.core.ui.component.picker.TeacherTimePicker
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalTime

@Composable
internal fun LessonTimePicker(
    label: String,
    time: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)
        Text(text = TimeUtils.format(time))
        TeacherTimePicker(
            time = time,
            onTimeSelected = onTimeSelected,
            label = { Text("Wybierz godzinę") },
        )
    }
}

@Preview
@Composable
private fun LessonTimePickerPreview() {
    TeacherAppTheme {
        Surface {
            var time by remember { mutableStateOf(TimeUtils.currentTime()) }

            LessonTimePicker(
                label = "Czas rozpoczęcia",
                time = time,
                onTimeSelected = { time = it },
            )
        }
    }
}