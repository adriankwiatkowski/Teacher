package com.example.teacher.feature.schedule.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schedule.R

@Composable
internal fun LessonPicker(
    lesson: Lesson?,
    onLessonPickerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = if (lesson != null) {
        val lessonName = lesson.name
        val schoolClassName = lesson.schoolClass.name
        "$lessonName $schoolClassName"
    } else {
        stringResource(R.string.schedule_pick_lesson)
    }

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.schedule_lesson),
            style = MaterialTheme.typography.labelMedium,
        )
        Column(Modifier.padding(MaterialTheme.spacing.small)) {
            if (lesson != null) {
                Text(
                    text = lesson.schoolClass.schoolYear.name,
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                label = text,
                onClick = onLessonPickerClick,
            )

            if (lesson == null) {
                Text(
                    text = stringResource(R.string.schedule_lesson_not_selected),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview
@Composable
private fun LessonPickerPreview(
    @PreviewParameter(
        LessonPreviewParameterProvider::class,
        limit = 1
    ) lesson: Lesson,
) {
    TeacherTheme {
        Surface {
            LessonPicker(lesson = lesson, onLessonPickerClick = {})
        }
    }
}