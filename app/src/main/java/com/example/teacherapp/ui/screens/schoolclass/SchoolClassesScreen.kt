package com.example.teacherapp.ui.screens.schoolclass

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.ui.components.form.TeacherChip
import com.example.teacherapp.ui.components.resource.ResultContent
import com.example.teacherapp.ui.screens.paramproviders.BasicSchoolClassesPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing
import com.example.teacherapp.ui.theme.warning

@Composable
fun SchoolClassesScreen(
    schoolClassesResult: Result<List<BasicSchoolClass>>,
    onAddSchoolClassClick: () -> Unit,
    onClassClick: (id: Long) -> Unit,
    onStudentsClick: (classId: Long) -> Unit,
    onLessonsClick: (classId: Long) -> Unit,
    addFabAction: (fabAction: FabAction) -> Unit,
    removeFabAction: (fabAction: FabAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    DisposableEffect(onAddSchoolClassClick) {
        val fabAction = FabAction(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            onClick = onAddSchoolClassClick,
        )
        addFabAction(fabAction)

        onDispose {
            removeFabAction(fabAction)
        }
    }

    ResultContent(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.small),
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

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
private fun ClassItem(
    name: String,
    studentCount: Int,
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
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Klasa: $name")

            val chipModifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.spacing.small)

            Row {
                TeacherChip(
                    modifier = chipModifier,
                    onClick = onStudentsClick,
                    leadingIcon = {
                        Icon(Icons.Outlined.Person, contentDescription = "")
                    }
                ) {
                    Text("Uczniowie ($studentCount)")
                }

                TeacherChip(
                    modifier = chipModifier,
                    onClick = onLessonsClick,
                    leadingIcon = {
                        Icon(Icons.Default.List, contentDescription = "")
                    }
                ) {
                    // TODO: Add lesson count.
                    Text("Zajęcia (0)")
                }
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
            text = "Nie istnieje jeszcze żadna klasa",
            style = MaterialTheme.typography.h4,
        )
        Icon(
            modifier = Modifier.weight(1f),
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colors.warning,
        )
    }
}

@Preview
@Composable
private fun SchoolClassesScreenPreview(
    @PreviewParameter(BasicSchoolClassesPreviewParameterProvider::class)
    basicSchoolClasses: List<BasicSchoolClass>,
) {
    TeacherAppTheme {
        Surface {
            SchoolClassesScreen(
                modifier = Modifier.fillMaxSize(),
                schoolClassesResult = Result.Success(basicSchoolClasses),
                onAddSchoolClassClick = {},
                onClassClick = {},
                onStudentsClick = {},
                onLessonsClick = {},
                addFabAction = {},
                removeFabAction = {},
            )
        }
    }
}