package com.example.teacherapp.feature.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.StudentGrade
import com.example.teacherapp.core.model.data.StudentGradesByLesson
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.StudentGradesByLessonPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.student.data.GradeDialogInfo

@Composable
internal fun StudentGradesScreen(
    studentGradesResult: Result<List<StudentGradesByLesson>>,
    gradeDialog: GradeDialogInfo?,
    onShowGradeDialog: (gradeInfo: StudentGradesByLesson, grade: StudentGrade) -> Unit,
    onGradeDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier.padding(MaterialTheme.spacing.small),
        result = studentGradesResult,
    ) { studentGradesByLesson ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (studentGradesByLesson.isEmpty()) {
                EmptyState()
            } else {
                if (gradeDialog != null) {
                    StudentGradeDialog(
                        student = gradeDialog.student,
                        gradeInfo = gradeDialog.gradeInfo,
                        grade = gradeDialog.grade,
                        onDismissRequest = onGradeDialogDismiss,
                    )
                }
            }

            MainScreen(
                studentGradesByLesson = studentGradesByLesson,
                onShowGradeDialog = onShowGradeDialog,
            )
        }
    }
}

@Composable
private fun MainScreen(
    studentGradesByLesson: List<StudentGradesByLesson>,
    onShowGradeDialog: (gradeInfo: StudentGradesByLesson, grade: StudentGrade) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        itemsIndexed(
            studentGradesByLesson,
            key = { _, item -> item.lessonId },
        ) { index, studentGrade ->
            Text(studentGrade.lessonName)
            Text("Średnia: ${studentGrade.average.toPlainString()}")
            Grades(
                grades = studentGrade.gradesByLessonId,
                onGradeClick = { grade -> onShowGradeDialog(studentGrade, grade) }
            )

            if (index != studentGradesByLesson.lastIndex) {
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Grades(
    grades: List<StudentGrade>,
    onGradeClick: (studentGrade: StudentGrade) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        for (grade in grades) {
            Grade(grade = grade.grade.toPlainString(), onClick = { onGradeClick(grade) })
        }
    }
}

@Composable
private fun Grade(
    grade: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TeacherButton(modifier = modifier, onClick = onClick) {
        Text(grade)
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Uczeń nie ma żadnej oceny",
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Preview
@Composable
private fun StudentGradesScreenPreview(
    @PreviewParameter(
        StudentGradesByLessonPreviewParameterProvider::class,
        limit = 1,
    ) studentGrades: List<StudentGradesByLesson>,
) {
    TeacherAppTheme {
        Surface {
            StudentGradesScreen(
                studentGradesResult = Result.Success(studentGrades),
                gradeDialog = null,
                onShowGradeDialog = { _, _ -> },
                onGradeDialogDismiss = {},
            )
        }
    }
}