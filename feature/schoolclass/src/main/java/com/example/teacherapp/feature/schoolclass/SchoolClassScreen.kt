package com.example.teacherapp.feature.schoolclass

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.provider.ActionItemProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.schoolclass.components.lessons
import com.example.teacherapp.feature.schoolclass.components.schoolYearExpandable
import com.example.teacherapp.feature.schoolclass.components.students
import com.example.teacherapp.feature.schoolclass.paramprovider.SchoolClassPreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolClassScreen(
    schoolClassResult: Result<SchoolClass>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onStudentClick: (id: Long) -> Unit,
    onAddStudentClick: () -> Unit,
    onLessonClick: (id: Long) -> Unit,
    onAddLessonClick: () -> Unit,
    isSchoolYearExpanded: MutableState<Boolean>,
    isStudentsExpanded: MutableState<Boolean>,
    isLessonsExpanded: MutableState<Boolean>,
    isSchoolClassDeleted: Boolean,
    onDeleteSchoolClassClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    val schoolClassName = remember(schoolClassResult) {
        (schoolClassResult as? Result.Success)?.data?.name.orEmpty()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TeacherTopBar(
                title = "Klasa $schoolClassName",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = listOf(
                    ActionItemProvider.delete(onDeleteSchoolClassClick)
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier.padding(innerPadding),
            result = schoolClassResult,
            isDeleted = isSchoolClassDeleted,
            deletedMessage = "Usunięto pomyślnie klasę."
        ) { schoolClass ->
            MainContent(
                schoolClass = schoolClass,
                onStudentClick = onStudentClick,
                onAddStudentClick = onAddStudentClick,
                onLessonClick = onLessonClick,
                onAddLessonClick = onAddLessonClick,
                isSchoolYearExpanded = isSchoolYearExpanded,
                isStudentsExpanded = isStudentsExpanded,
                isLessonsExpanded = isLessonsExpanded,
            )
        }
    }
}

@Composable
private fun MainContent(
    schoolClass: SchoolClass,
    onStudentClick: (id: Long) -> Unit,
    onAddStudentClick: () -> Unit,
    onLessonClick: (id: Long) -> Unit,
    onAddLessonClick: () -> Unit,
    isSchoolYearExpanded: MutableState<Boolean>,
    isStudentsExpanded: MutableState<Boolean>,
    isLessonsExpanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
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
            studentCount = schoolClass.students.size.toLong(),
            onLessonClick = onLessonClick,
            onAddLessonClick = onAddLessonClick,
            expanded = isLessonsExpanded,
        )
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
                showNavigationIcon = true,
                onNavBack = {},
                onStudentClick = {},
                onAddStudentClick = {},
                onLessonClick = {},
                onAddLessonClick = {},
                isSchoolYearExpanded = isSchoolYearExpanded,
                isStudentsExpanded = isStudentsExpanded,
                isLessonsExpanded = isLessonsExpanded,
                isSchoolClassDeleted = false,
                onDeleteSchoolClassClick = {},
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
                showNavigationIcon = true,
                onNavBack = {},
                onStudentClick = {},
                onAddStudentClick = {},
                onLessonClick = {},
                onAddLessonClick = {},
                isSchoolYearExpanded = isSchoolYearExpanded,
                isStudentsExpanded = isStudentsExpanded,
                isLessonsExpanded = isLessonsExpanded,
                isSchoolClassDeleted = true,
                onDeleteSchoolClassClick = {},
            )
        }
    }
}