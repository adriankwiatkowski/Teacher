package com.example.teacherapp.ui.screens.schoolclass

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.SchoolClass
import com.example.teacherapp.ui.components.resource.ResourceContent
import com.example.teacherapp.ui.screens.paramproviders.SchoolClassPreviewParameterProvider
import com.example.teacherapp.ui.screens.schoolclass.components.*
import com.example.teacherapp.ui.theme.TeacherAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchoolClassScreen(
    schoolClassResource: Resource<SchoolClass>,
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
    ResourceContent(
        modifier = modifier,
        resource = schoolClassResource,
        isDeleted = isSchoolClassDeleted,
        deletedMessage = "Usunięto pomyślnie klasę."
    ) { schoolClass ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
        ) {
            stickyHeader {
                Text("Klasa ${schoolClass.name}", style = MaterialTheme.typography.h4)
                Spacer(modifier = Modifier.padding(8.dp))
            }

            schoolYearExpandable(
                schoolYear = schoolClass.schoolYear,
                expanded = isSchoolYearExpanded,
            )

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            students(
                students = schoolClass.students,
                onStudentClick = onStudentClick,
                onAddStudentClick = onAddStudentClick,
                expanded = isStudentsExpanded,
            )

            item {
                Spacer(modifier = Modifier.height(8.dp))
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
                schoolClassResource = Resource.Success(schoolClass),
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
                schoolClassResource = Resource.Loading,
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