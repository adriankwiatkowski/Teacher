package com.example.teacherapp.feature.lesson.gradetemplate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
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
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.core.ui.component.TeacherFab
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.BasicGradeTemplatesPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@Composable
internal fun GradeTemplatesScreen(
    gradesResult: Result<List<BasicGradeTemplate>>,
    snackbarHostState: SnackbarHostState,
    onGradeClick: (gradeId: Long) -> Unit,
    onAddGradeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ResultContent(
        modifier = modifier,
        result = gradesResult,
    ) { grades ->
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
                grades = grades,
                onGradeClick = onGradeClick,
            )
        }
    }
}

@Composable
private fun MainContent(
    grades: List<BasicGradeTemplate>,
    onGradeClick: (gradeId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        itemsIndexed(grades, key = { _, item -> item.id }) { index, grade ->
            ListItem(
                modifier = Modifier.clickable(onClick = { onGradeClick(grade.id) }),
                headlineContent = { Text(grade.name) },
                supportingContent = { Text("Waga ${grade.weight}") },
            )

            if (index != grades.lastIndex) {
                Divider()
            }
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
    TeacherAppTheme {
        Surface {
            GradeTemplatesScreen(
                gradesResult = Result.Success(grades),
                snackbarHostState = remember { SnackbarHostState() },
                onGradeClick = {},
                onAddGradeClick = {},
            )
        }
    }
}