package com.example.teacher.feature.student

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGrade
import com.example.teacher.core.model.data.StudentGradesByLesson
import com.example.teacher.core.ui.paramprovider.StudentGradesByLessonPreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.StudentPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
internal fun StudentGradeDialog(
    student: Student,
    gradeInfo: StudentGradesByLesson,
    grade: StudentGrade,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(student.fullName) },
        text = {
            Column {
                Text(text = gradeInfo.lessonName, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = gradeInfo.average.toPlainString(),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(text = grade.gradeName, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = grade.grade.toPlainString(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(text = grade.weight.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}

@Preview
@Composable
private fun StudentGradeDialogPreview(
    @PreviewParameter(
        StudentGradesByLessonPreviewParameterProvider::class,
        limit = 1,
    ) studentGrades: List<StudentGradesByLesson>,
) {
    val student = StudentPreviewParameterProvider().values.first()
    val gradeInfo = studentGrades.first()
    val grades = gradeInfo.gradesByLessonId
    val grade = grades.first()

    TeacherTheme {
        Surface {
            StudentGradeDialog(
                student = student,
                gradeInfo = gradeInfo,
                grade = grade,
                onDismissRequest = {},
            )
        }
    }
}