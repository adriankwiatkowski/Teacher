package com.example.teacherapp.ui.screens.grade

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.components.resource.ResultContent
import com.example.teacherapp.ui.screens.paramproviders.BasicGradesForTemplatePreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing
import java.math.BigDecimal

@Composable
fun GradesScreen(
    gradesResult: Result<List<BasicGradeForTemplate>>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TeacherTopBar(
                title = "Polski 2A", // TODO: Set actual title.
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = listOf(
                    ActionMenuItemProvider.edit(onEditClick),
                    ActionMenuItemProvider.delete(onDeleteClick),
                ),
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier.padding(innerPadding),
            result = gradesResult,
            isDeleted = isDeleted,
            deletedMessage = "Usunięto ocenę",
        ) { grades ->
            MainContent(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                grades = grades,
            )
        }
    }
}

@Composable
private fun MainContent(
    grades: List<BasicGradeForTemplate>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        itemsIndexed(grades, key = { _, grade -> grade.studentId }) { index, grade ->
            GradeItem(fullName = grade.studentFullName, grade = gradeToName(grade.grade))

            if (index != grades.lastIndex) {
                Divider()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GradeItem(
    fullName: String,
    grade: String,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        text = { Text("$fullName ($grade)") }
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
    TeacherAppTheme {
        Surface {
            GradesScreen(
                gradesResult = Result.Success(grades),
                showNavigationIcon = true,
                onNavBack = {},
                onEditClick = {},
                onDeleteClick = {},
                isDeleted = false,
            )
        }
    }
}