package com.example.teacher.feature.grade

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.GradeTemplateInfo
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.BasicGradesForTemplatePreviewParameterProvider
import com.example.teacher.core.ui.provider.ActionItemProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.grade.data.GradesUiState
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GradesScreen(
    uiStateResult: Result<GradesUiState>,
    snackbarHostState: SnackbarHostState,
    onStudentClick: (studentId: Long, gradeId: Long?) -> Unit,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    val title = remember(uiStateResult) {
        (uiStateResult as? Result.Success)?.data?.gradeTemplateInfo?.let { info ->
            "${info.lessonName} ${info.schoolClassName}"
        } ?: "Wystawienie ocen"
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = title,
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = listOf(
                    ActionItemProvider.edit(onEditClick),
                    ActionItemProvider.delete(onDeleteClick),
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier.padding(innerPadding),
            result = uiStateResult,
            isDeleted = isDeleted,
            deletedMessage = "Usunięto ocenę",
        ) { uiState ->
            MainContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small),
                gradeName = uiState.gradeTemplateInfo.gradeName,
                grades = uiState.grades,
                onStudentClick = onStudentClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MainContent(
    gradeName: String,
    grades: List<BasicGradeForTemplate>,
    onStudentClick: (studentId: Long, gradeId: Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        stickyHeader {
            Text(text = gradeName)
        }

        itemsIndexed(grades, key = { _, grade -> grade.studentId }) { index, grade ->
            GradeItem(
                fullName = grade.studentFullName,
                grade = gradeToName(grade.grade),
                onClick = { onStudentClick(grade.studentId, grade.id) },
            )

            if (index != grades.lastIndex) {
                Divider()
            }
        }
    }
}

@Composable
private fun GradeItem(
    fullName: String,
    grade: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        headlineContent = { Text("$fullName ($grade)") },
//        text = { Text(fullName) },
//        secondaryText = { Text(grade) },
    )
}

private fun gradeToName(grade: BigDecimal?): String = grade?.toString() ?: "brak oceny"

@Preview
@Composable
private fun GradesScreenPreview(
    @PreviewParameter(
        BasicGradesForTemplatePreviewParameterProvider::class,
        limit = 1,
    ) grades: List<BasicGradeForTemplate>,
) {
    TeacherTheme {
        Surface {
            val uiState = remember {
                GradesUiState(
                    grades = grades,
                    gradeTemplateInfo = GradeTemplateInfo(
                        gradeTemplateId = 1L,
                        gradeName = "Dodawanie",
                        gradeWeight = 3,
                        lessonId = 1L,
                        lessonName = "Matematyka",
                        schoolClassId = 1L,
                        schoolClassName = "1A",
                        isFirstTerm = true,
                    ),
                )
            }

            GradesScreen(
                uiStateResult = Result.Success(uiState),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onEditClick = {},
                onDeleteClick = {},
                isDeleted = false,
                onStudentClick = { _, _ -> }
            )
        }
    }
}