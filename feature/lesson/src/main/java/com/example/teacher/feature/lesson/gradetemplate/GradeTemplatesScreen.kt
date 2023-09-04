package com.example.teacher.feature.lesson.gradetemplate

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.BasicGradeTemplatesPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.gradetemplate.data.GradeTemplatesUiState

@Composable
internal fun GradeTemplatesScreen(
    gradeTemplatesResult: Result<GradeTemplatesUiState>,
    snackbarHostState: SnackbarHostState,
    onGradeClick: (gradeId: Long) -> Unit,
    onAddGradeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier,
        result = gradeTemplatesResult,
    ) { gradeTemplates ->
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                TeacherFab(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    onClick = onAddGradeClick,
                )
            },
        ) { innerPadding ->
            MainContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                firstTermGrades = gradeTemplates.firstTermGrades,
                secondTermGrades = gradeTemplates.secondTermGrades,
                onGradeClick = onGradeClick,
            )
        }
    }
}

@Composable
private fun MainContent(
    firstTermGrades: List<BasicGradeTemplate>,
    secondTermGrades: List<BasicGradeTemplate>,
    onGradeClick: (gradeId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        // TODO: Don't hardcode term name.
        termHeader("Semestr I")
        grades(grades = firstTermGrades, onGradeClick = onGradeClick)

        // TODO: Don't hardcode term name.
        termHeader("Semestr II")
        grades(grades = secondTermGrades, onGradeClick = onGradeClick)
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.termHeader(text: String) {
    stickyHeader {
        Surface(modifier = Modifier.fillParentMaxWidth()) {
            Text(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                style = MaterialTheme.typography.headlineSmall,
                text = text,
            )
        }
    }
}

private fun LazyListScope.grades(
    grades: List<BasicGradeTemplate>,
    onGradeClick: (gradeId: Long) -> Unit,
) {
    items(grades, key = { item -> item.id }) { grade ->
        ListItem(
            modifier = Modifier.clickable(onClick = { onGradeClick(grade.id) }),
            headlineContent = { Text(grade.name) },
            supportingContent = { Text("Waga ${grade.weight}") },
        )
    }

    if (grades.isEmpty()) {
        item {
            ListItem(headlineContent = { Text("Brak ocen w semestrze") })
        }
    }
}

@Preview
@Composable
private fun GradeTemplatesScreenPreview(
    @PreviewParameter(
        BasicGradeTemplatesPreviewParameterProvider::class,
        limit = 5,
    ) grades: List<BasicGradeTemplate>
) {
    TeacherTheme {
        Surface {
            val gradeTemplates = remember(grades) {
                GradeTemplatesUiState(
                    firstTermGrades = grades.filter { grade -> grade.isFirstTerm },
                    secondTermGrades = grades.filter { grade -> !grade.isFirstTerm }
                )
            }

            GradeTemplatesScreen(
                gradeTemplatesResult = Result.Success(gradeTemplates),
                snackbarHostState = remember { SnackbarHostState() },
                onGradeClick = {},
                onAddGradeClick = {},
            )
        }
    }
}