package com.example.teacher.feature.schoolclass

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.paramprovider.BasicStudentsPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.schoolclass.components.StudentItem

@Composable
internal fun SchoolClassStudentsScreen(
    snackbarHostState: SnackbarHostState,
    students: List<BasicStudent>,
    onStudentClick: (studentId: Long) -> Unit,
    onAddStudentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = { TeacherFab(TeacherActions.add(onClick = onAddStudentClick)) },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        MainScreen(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small),
            students = students,
            onStudentClick = onStudentClick,
        )
    }
}

@Composable
private fun MainScreen(
    students: List<BasicStudent>,
    onStudentClick: (studentId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (students.isEmpty()) {
        EmptyState(modifier = modifier)
        return
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        items(items = students, key = { student -> student.id }) { student ->
            StudentItem(
                name = student.name,
                surname = student.surname,
                email = student.email,
                phone = student.phone,
                onClick = { onStudentClick(student.id) },
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TeacherLargeText(text = stringResource(R.string.school_class_no_students_in_class))
    }
}

@Preview
@Composable
private fun SchoolClassStudentsScreenPreview(
    @PreviewParameter(
        BasicStudentsPreviewParameterProvider::class,
        limit = 1,
    ) students: List<BasicStudent>,
) {
    TeacherTheme {
        Surface {
            SchoolClassStudentsScreen(
                snackbarHostState = remember { SnackbarHostState() },
                students = students,
                onStudentClick = {},
                onAddStudentClick = {},
            )
        }
    }
}