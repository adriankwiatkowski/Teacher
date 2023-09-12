package com.example.teacher.feature.schoolclass

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.SchoolClassPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schoolclass.components.lessons
import com.example.teacher.feature.schoolclass.components.schoolYearExpandable
import com.example.teacher.feature.schoolclass.components.students

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SchoolClassScreen(
    schoolClassResult: Result<SchoolClass>,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onEditSchoolClassClick: () -> Unit,
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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = stringResource(R.string.school_class, schoolClassName),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = listOf(
                    TeacherActions.edit(onEditSchoolClassClick),
                    TeacherActions.delete(onDeleteSchoolClassClick)
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            result = schoolClassResult,
            isDeleted = isSchoolClassDeleted,
            deletedMessage = stringResource(R.string.school_class_school_class_deleted),
        ) { schoolClass ->
            MainContent(
                modifier = Modifier.fillMaxSize(),
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
    val studentsLabel = stringResource(R.string.school_class_students, schoolClass.students.size)
    val lessonsLabel = stringResource(R.string.school_class_lessons, schoolClass.lessons.size)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
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
            label = studentsLabel,
            students = schoolClass.students,
            onStudentClick = onStudentClick,
            onAddStudentClick = onAddStudentClick,
            expanded = isStudentsExpanded,
        )

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        }

        lessons(
            label = lessonsLabel,
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

    TeacherTheme {
        Surface {
            SchoolClassScreen(
                schoolClassResult = Result.Success(schoolClass),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onEditSchoolClassClick = {},
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

    TeacherTheme {
        Surface {
            SchoolClassScreen(
                schoolClassResult = Result.Loading,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onEditSchoolClassClick = {},
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