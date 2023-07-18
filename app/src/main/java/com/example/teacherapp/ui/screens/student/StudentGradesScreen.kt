package com.example.teacherapp.ui.screens.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.StudentGrade
import com.example.teacherapp.core.model.data.StudentGradesByLesson
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.result.ResultContent
import com.example.teacherapp.ui.screens.paramproviders.StudentGradesByLessonPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun StudentGradesScreen(
    studentGradesResult: Result<List<StudentGradesByLesson>>,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier.padding(MaterialTheme.spacing.small),
        result = studentGradesResult,
    ) { studentGradesByLesson ->
        if (studentGradesByLesson.isEmpty()) {
            EmptyState()
        } else {
            MainScreen(studentGradesByLesson)
        }
    }
}

@Composable
private fun MainScreen(
    studentGradesByLesson: List<StudentGradesByLesson>,
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
            Grades(grades = studentGrade.gradesByLessonId)

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
    modifier: Modifier = Modifier,
) {
    FlowRow(modifier = modifier) {
        for (grade in grades) {
            TextButton(onClick = { /*TODO*/ }) {
                Grade(grade = grade.grade.toPlainString(), onClick = {})
            }
        }
    }
}

@Composable
private fun Grade(
    grade: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TeacherOutlinedButton(modifier = modifier, onClick = onClick) {
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
            )
        }
    }
}