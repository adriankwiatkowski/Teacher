package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.LessonActivity

class LessonActivitiesPreviewParameterProvider : PreviewParameterProvider<List<LessonActivity>> {
    override val values: Sequence<List<LessonActivity>> = sequenceOf(
        LessonActivityPreviewParameterProvider().values.toList(),
        emptyList(),
    )
}

class LessonActivityPreviewParameterProvider : PreviewParameterProvider<LessonActivity> {
    override val values: Sequence<LessonActivity> = students
        .flatMap { student ->
            val lessonActivity = LessonActivity(
                id = null,
                lesson = BasicLesson(
                    id = 1L,
                    name = "1A",
                    schoolClassId = student.classId,
                ),
                student = student,
                sum = null,
                isFirstTerm = true,
            )

            listOf(
                lessonActivity,
                lessonActivity.copy(isFirstTerm = false),
            )
        }

    companion object {
        private val students = BasicStudentPreviewParameterProvider().values
    }
}