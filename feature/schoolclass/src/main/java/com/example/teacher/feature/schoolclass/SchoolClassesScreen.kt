package com.example.teacher.feature.schoolclass

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
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
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.ui.component.TeacherChip
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.BasicSchoolClassesPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@Composable
internal fun SchoolClassesScreen(
    schoolClassesResult: Result<List<BasicSchoolClass>>,
    snackbarHostState: SnackbarHostState,
    onAddSchoolClassClick: () -> Unit,
    onClassClick: (id: Long) -> Unit,
    onStudentsClick: (classId: Long) -> Unit,
    onLessonsClick: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = { TeacherFab(TeacherActions.add(onClick = onAddSchoolClassClick)) },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small)
                .fillMaxSize(),
            result = schoolClassesResult,
        ) { schoolClasses ->
            MainContent(
                schoolClasses = schoolClasses,
                onClassClick = onClassClick,
                onStudentsClick = onStudentsClick,
                onLessonsClick = onLessonsClick,
            )
        }
    }
}

@Composable
private fun MainContent(
    schoolClasses: List<BasicSchoolClass>,
    onClassClick: (id: Long) -> Unit,
    onStudentsClick: (classId: Long) -> Unit,
    onLessonsClick: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        items(schoolClasses, key = { it.id }) { schoolClass ->
            ClassItem(
                name = schoolClass.name,
                studentCount = schoolClass.studentCount,
                lessonCount = schoolClass.lessonCount,
                onClick = { onClassClick(schoolClass.id) },
                onStudentsClick = { onStudentsClick(schoolClass.id) },
                onLessonsClick = { onLessonsClick(schoolClass.id) },
            )
        }

        if (schoolClasses.isEmpty()) {
            item {
                EmptyClasses(Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun ClassItem(
    name: String,
    studentCount: Long,
    lessonCount: Long,
    onClick: () -> Unit,
    onStudentsClick: () -> Unit,
    onLessonsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
    ) {
        FlowRow(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(R.string.school_class, name))

            val chipModifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.spacing.small)

            Row {
                TeacherChip(
                    modifier = chipModifier,
                    label = stringResource(R.string.students, studentCount),
                    onClick = onStudentsClick,
                    leadingIcon = {
                        val icon = TeacherIcons.person()
                        Icon(icon.icon, stringResource(icon.text))
                    },
                )

                TeacherChip(
                    modifier = chipModifier,
                    label = stringResource(R.string.lessons, lessonCount),
                    onClick = onLessonsClick,
                    leadingIcon = {
                        val icon = TeacherIcons.subject()
                        Icon(icon.icon, stringResource(icon.text))
                    },
                )
            }
        }
    }
}

@Composable
private fun EmptyClasses(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(9f),
            text = stringResource(R.string.school_classes_empty),
            style = MaterialTheme.typography.displayLarge,
        )

        val icon = TeacherIcons.warning()
        Icon(
            modifier = Modifier.weight(1f),
            imageVector = icon.icon,
            contentDescription = stringResource(icon.text),
        )
    }
}

@Preview
@Composable
private fun SchoolClassesScreenPreview(
    @PreviewParameter(BasicSchoolClassesPreviewParameterProvider::class)
    basicSchoolClasses: List<BasicSchoolClass>,
) {
    TeacherTheme {
        Surface {
            SchoolClassesScreen(
                modifier = Modifier.fillMaxSize(),
                snackbarHostState = remember { SnackbarHostState() },
                schoolClassesResult = Result.Success(basicSchoolClasses),
                onAddSchoolClassClick = {},
                onClassClick = {},
                onStudentsClick = {},
                onLessonsClick = {},
            )
        }
    }
}