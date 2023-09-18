package com.example.teacher.feature.student

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGradeInfo
import com.example.teacher.core.model.data.StudentGradesByLesson
import com.example.teacher.core.ui.paramprovider.StudentGradesByLessonPreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.StudentPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@Composable
internal fun StudentGradeDialog(
    student: Student,
    gradeInfo: StudentGradesByLesson,
    grade: StudentGradeInfo,
    termString: @Composable () -> String,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(student.fullName) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                Text(text = gradeInfo.lessonName, style = MaterialTheme.typography.titleMedium)
                Text(text = grade.gradeName, style = MaterialTheme.typography.titleMedium)

                Text(text = termString(), style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

                val actualGrade = grade.grade?.grade
                val gradeString = if (actualGrade != null) {
                    DecimalUtils.toGrade(actualGrade)
                } else {
                    stringResource(R.string.student_empty_grade)
                }

                Text(
                    text = stringResource(R.string.student_grade, gradeString),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(R.string.student_weight_with_data, grade.weight),
                    style = MaterialTheme.typography.bodyLarge,
                )
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
    val grades = gradeInfo.firstTermGrades
    val grade = grades.first()

    TeacherTheme {
        Surface {
            StudentGradeDialog(
                student = student,
                gradeInfo = gradeInfo,
                grade = grade,
                termString = { "Semestr ${if (grade.isFirstTerm) "I" else "II"}" },
                onDismissRequest = {},
            )
        }
    }
}