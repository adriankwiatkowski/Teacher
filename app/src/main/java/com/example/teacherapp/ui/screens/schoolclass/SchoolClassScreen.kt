package com.example.teacherapp.ui.screens.schoolclass

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.ui.components.resource.ResultContent
import com.example.teacherapp.ui.screens.paramproviders.SchoolClassPreviewParameterProvider
import com.example.teacherapp.ui.screens.schoolclass.components.lessons
import com.example.teacherapp.ui.screens.schoolclass.components.schoolYearExpandable
import com.example.teacherapp.ui.screens.schoolclass.components.students
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun SchoolClassScreen(
    schoolClassResult: Result<SchoolClass>,
    onStudentClick: (id: Long) -> Unit,
    onAddStudentClick: () -> Unit,
    onLessonClick: (id: Long) -> Unit,
    onAddLessonClick: () -> Unit,
    isSchoolYearExpanded: MutableState<Boolean>,
    isStudentsExpanded: MutableState<Boolean>,
    isLessonsExpanded: MutableState<Boolean>,
    isSchoolClassDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier,
        result = schoolClassResult,
        isDeleted = isSchoolClassDeleted,
        deletedMessage = "Usunięto pomyślnie klasę."
    ) { schoolClass ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(MaterialTheme.spacing.small),
        ) {
            schoolYearExpandable(
                schoolYear = schoolClass.schoolYear,
                expanded = isSchoolYearExpanded,
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            students(
                students = schoolClass.students,
                onStudentClick = onStudentClick,
                onAddStudentClick = onAddStudentClick,
                expanded = isStudentsExpanded,
            )

            item {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            }

            lessons(
                lessons = schoolClass.lessons,
                onLessonClick = onLessonClick,
                onAddLessonClick = onAddLessonClick,
                expanded = isLessonsExpanded,
            )
        }
    }
}

@Preview
@Composable
private fun SchoolClassScreenPreview(
    @PreviewParameter(
        SchoolClassPreviewParameterProvider::class,
        limit = 1,
    ) schoolClass: SchoolClass,
) {
    val isSchoolYearExpanded = remember { mutableStateOf(false) }
    val isStudentsExpanded = remember { mutableStateOf(false) }
    val isLessonsExpanded = remember { mutableStateOf(false) }

    TeacherAppTheme {
        Surface {
            SchoolClassScreen(
                modifier = Modifier.fillMaxSize(),
                schoolClassResult = Result.Success(schoolClass),
                onStudentClick = {},
                onAddStudentClick = {},
                onLessonClick = {},
                onAddLessonClick = {},
                isSchoolYearExpanded = isSchoolYearExpanded,
                isStudentsExpanded = isStudentsExpanded,
                isLessonsExpanded = isLessonsExpanded,
                isSchoolClassDeleted = false
            )
        }
    }
}

@Preview
@Composable
private fun SchoolClassScreenDeletedPreview() {
    val isSchoolYearExpanded = remember { mutableStateOf(false) }
    val isStudentsExpanded = remember { mutableStateOf(false) }
    val isLessonsExpanded = remember { mutableStateOf(false) }

    TeacherAppTheme {
        Surface {
            SchoolClassScreen(
                modifier = Modifier.fillMaxSize(),
                schoolClassResult = Result.Loading,
                onStudentClick = {},
                onAddStudentClick = {},
                onLessonClick = {},
                onAddLessonClick = {},
                isSchoolYearExpanded = isSchoolYearExpanded,
                isStudentsExpanded = isStudentsExpanded,
                isLessonsExpanded = isLessonsExpanded,
                isSchoolClassDeleted = true,
            )
        }
    }
}