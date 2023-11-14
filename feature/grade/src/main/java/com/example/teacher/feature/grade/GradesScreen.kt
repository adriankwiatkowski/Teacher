package com.example.teacher.feature.grade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.BasicGradesForTemplatePreviewParameterProvider
import com.example.teacher.core.ui.paramprovider.GradeTemplateInfoPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
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
    val context = LocalContext.current
    val scrollBehavior = TeacherTopBarDefaults.default()

    val title = remember(context, uiStateResult) {
        (uiStateResult as? Result.Success)?.data?.gradeTemplateInfo?.let { info ->
            "${info.lesson.name} ${info.lesson.schoolClass.name}"
        } ?: context.getString(R.string.grade_grades_title)
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
                    TeacherActions.edit(onEditClick),
                    TeacherActions.delete(onDeleteClick),
                ),
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier.padding(innerPadding),
            result = uiStateResult,
            isDeleted = isDeleted,
            deletedMessage = stringResource(R.string.grade_grade_deleted),
        ) { uiState ->
            MainContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.spacing.small),
                gradeName = uiState.gradeTemplateInfo.gradeName,
                gradeAverage = uiState.gradeTemplateInfo.averageGrade,
                gradeDescription = uiState.gradeTemplateInfo.gradeDescription,
                gradeTermName = if (uiState.gradeTemplateInfo.isFirstTerm) {
                    uiState.gradeTemplateInfo.lesson.schoolClass.schoolYear.firstTerm.name
                } else {
                    uiState.gradeTemplateInfo.lesson.schoolClass.schoolYear.secondTerm.name
                },
                grades = uiState.grades,
                onStudentClick = onStudentClick,
            )
        }
    }
}

@Composable
private fun MainContent(
    gradeName: String,
    gradeAverage: BigDecimal?,
    gradeDescription: String?,
    gradeTermName: String,
    grades: List<BasicGradeForTemplate>,
    onStudentClick: (studentId: Long, gradeId: Long?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        gradeName(gradeName = gradeName, gradeTermName = gradeTermName)
        gradeAverage(gradeAverage = gradeAverage)
        gradeDescription(gradeDescription = gradeDescription)

        items(grades, key = { grade -> grade.studentId }) { grade ->
            GradeItem(
                fullName = grade.studentFullName,
                grade = gradeToName(grade.grade),
                onClick = { onStudentClick(grade.studentId, grade.id) },
            )
        }

        if (grades.isEmpty()) {
            item {
                TeacherLargeText(stringResource(R.string.grade_no_students))
            }
        }
    }
}

private fun LazyListScope.gradeName(gradeName: String, gradeTermName: String) {
    item {
        Text(
            text = stringResource(R.string.grades_grade_with_term, gradeName, gradeTermName),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

private fun LazyListScope.gradeAverage(gradeAverage: BigDecimal?) {
    if (gradeAverage != null) {
        item {
            val text =
                stringResource(R.string.grades_grade_average, DecimalUtils.toLiteral(gradeAverage))
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

private fun LazyListScope.gradeDescription(gradeDescription: String?) {
    if (gradeDescription != null) {
        item {
            Text(text = gradeDescription, style = MaterialTheme.typography.bodyMedium)
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
    )
}

@Composable
private fun gradeToName(grade: BigDecimal?): String {
    return grade?.let(DecimalUtils::toGrade) ?: stringResource(R.string.grade_no_grade)
}

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
                    gradeTemplateInfo = GradeTemplateInfoPreviewParameterProvider().values.first(),
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