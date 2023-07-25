package com.example.teacherapp.feature.lesson.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacherapp.core.model.data.BasicLesson
import com.example.teacherapp.core.model.data.LessonActivity

internal class LessonActivitiesPreviewParameterProvider :
    PreviewParameterProvider<List<LessonActivity>> {
    override val values: Sequence<List<LessonActivity>> = sequenceOf(
        LessonActivityPreviewParameterProvider().values.toList()
    )
}

internal class LessonActivityPreviewParameterProvider : PreviewParameterProvider<LessonActivity> {
    override val values: Sequence<LessonActivity> = students
        .map { student ->
            LessonActivity(
                id = null,
                lesson = BasicLesson(
                    id = 1L,
                    name = "1A",
                    schoolClassId = student.classId,
                ),
                student = student,
                sum = null,
            )
        }

    companion object {
        private val students = BasicStudentPreviewParameterProvider().values
    }
}