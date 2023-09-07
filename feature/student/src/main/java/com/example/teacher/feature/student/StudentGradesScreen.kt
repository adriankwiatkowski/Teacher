package com.example.teacher.feature.student

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.StudentGrade
import com.example.teacher.core.model.data.StudentGradesByLesson
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.StudentGradesByLessonPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.student.data.GradeDialogInfo
import java.math.BigDecimal

@Composable
internal fun StudentGradesScreen(
    studentGradesResult: Result<List<StudentGradesByLesson>>,
    snackbarHostState: SnackbarHostState,
    gradeDialog: GradeDialogInfo?,
    onShowGradeDialog: (gradeInfo: StudentGradesByLesson, grade: StudentGrade) -> Unit,
    onGradeDialogDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        ResultContent(
            modifier = modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
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
        items(
            studentGradesByLesson,
            key = { item -> item.lessonId },
        ) { lessonGrades ->
            LessonGradesCard(lessonGrades = lessonGrades, onShowGradeDialog = onShowGradeDialog)
        }
    }
}

@Composable
private fun LessonGradesCard(
    lessonGrades: StudentGradesByLesson,
    onShowGradeDialog: (gradeInfo: StudentGradesByLesson, grade: StudentGrade) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
            Text(lessonGrades.lessonName)

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            LessonTermGrades(
                termLabel = "Pierwszy semestr",
                grades = lessonGrades.firstTermGrades,
                average = lessonGrades.firstTermAverage,
                onShowGradeDialog = { grade -> onShowGradeDialog(lessonGrades, grade) },
            )

            Divider()

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

            LessonTermGrades(
                termLabel = "Drugi semestr",
                grades = lessonGrades.secondTermGrades,
                average = lessonGrades.secondTermAverage,
                onShowGradeDialog = { grade -> onShowGradeDialog(lessonGrades, grade) },
            )
        }
    }
}

@Composable
private fun LessonTermGrades(
    termLabel: String,
    grades: List<StudentGrade>,
    average: BigDecimal?,
    onShowGradeDialog: (grade: StudentGrade) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = termLabel, style = MaterialTheme.typography.labelMedium)

        if (grades.isNotEmpty() && average != null) {
            Text(text = stringResource(R.string.student_grade_average, average.toPlainString()))
            Grades(
                grades = grades,
                onGradeClick = { grade -> onShowGradeDialog(grade) }
            )
        } else {
            Text(stringResource(R.string.student_no_grades))
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
    TeacherButton(modifier = modifier, label = grade, onClick = onClick)
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TeacherLargeText(text = stringResource(R.string.student_empty_grades))
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
    TeacherTheme {
        Surface {
            StudentGradesScreen(
                studentGradesResult = Result.Success(studentGrades),
                snackbarHostState = remember { SnackbarHostState() },
                gradeDialog = null,
                onShowGradeDialog = { _, _ -> },
                onGradeDialogDismiss = {},
            )
        }
    }
}