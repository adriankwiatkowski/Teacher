package com.example.teacher.feature.schedule.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.ui.component.picker.TeacherDayPicker
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schedule.R
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
        Text(text = stringResource(R.string.schedule_day))
        TeacherDayPicker(
            day = day,
            onDaySelected = onDaySelected,
            label = TimeUtils.getDisplayNameOfDayOfWeek(day),
        )
    }
}

@Preview
@Composable
private fun LessonDayPickerPreview() {
    TeacherTheme {
        Surface {
            var day by remember { mutableStateOf(TimeUtils.monday()) }

            LessonDayPicker(
                day = day,
                onDaySelected = { day = it },
            )
        }
    }
}