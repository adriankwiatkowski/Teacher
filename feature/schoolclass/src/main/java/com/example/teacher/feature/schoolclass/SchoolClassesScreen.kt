package com.example.teacher.feature.schoolclass

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
import com.example.teacher.core.model.data.SchoolClassesByYear
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.TextWithIcon
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.SchoolClassesByYearPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@Composable
internal fun SchoolClassesScreen(
    schoolClassesResult: Result<List<SchoolClassesByYear>>,
    snackbarHostState: SnackbarHostState,
    onAddSchoolClassClick: () -> Unit,
    onClassClick: (id: Long) -> Unit,
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
            MainContent(schoolClassesByYear = schoolClasses, onClassClick = onClassClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainContent(
    schoolClassesByYear: List<SchoolClassesByYear>,
    onClassClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        for (schoolClasses in schoolClassesByYear) {
            stickyHeader {
                Surface(modifier = Modifier.fillParentMaxWidth()) {
                    Text(
                        modifier = Modifier.padding(MaterialTheme.spacing.small),
                        style = MaterialTheme.typography.headlineSmall,
                        text = schoolClasses.year.name,
                    )
                }
            }
            items(schoolClasses.schoolClasses, key = { it.id }) { schoolClass ->
                ClassItem(
                    name = schoolClass.name,
                    studentCount = schoolClass.studentCount,
                    lessonCount = schoolClass.lessonCount,
                    onClick = { onClassClick(schoolClass.id) },
                )
            }
        }

        if (schoolClassesByYear.isEmpty()) {
            item {
                // TODO: Center Empty state.
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
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.school_class, name),
                style = MaterialTheme.typography.headlineSmall,
            )

            FlowRow(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalArrangement = Arrangement.Center,
            ) {
                TextWithIcon(
                    text = stringResource(R.string.school_class_students_with_data, studentCount),
                    icon = TeacherIcons.people()
                )
                TextWithIcon(
                    text = stringResource(R.string.school_class_lessons, lessonCount),
                    icon = TeacherIcons.subject()
                )
            }
        }
    }
}

@Composable
private fun EmptyClasses(modifier: Modifier = Modifier) {
    TeacherLargeText(modifier = modifier, text = stringResource(R.string.school_classes_empty))
}

@Preview
@Composable
private fun SchoolClassesScreenPreview(
    @PreviewParameter(SchoolClassesByYearPreviewParameterProvider::class)
    schoolClassesByYear: List<SchoolClassesByYear>,
) {
    TeacherTheme {
        Surface {
            SchoolClassesScreen(
                modifier = Modifier.fillMaxSize(),
                snackbarHostState = remember { SnackbarHostState() },
                schoolClassesResult = Result.Success(schoolClassesByYear),
                onAddSchoolClassClick = {},
                onClassClick = {},
            )
        }
    }
}