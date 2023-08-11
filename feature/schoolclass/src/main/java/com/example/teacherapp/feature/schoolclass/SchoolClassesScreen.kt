package com.example.teacherapp.feature.schoolclass

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.ui.component.TeacherChip
import com.example.teacherapp.core.ui.component.TeacherFab
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.paramprovider.BasicSchoolClassesPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@Composable
internal fun SchoolClassesScreen(
    schoolClassesResult: Result<List<BasicSchoolClass>>,
    onAddSchoolClassClick: () -> Unit,
    onClassClick: (id: Long) -> Unit,
    onStudentsClick: (classId: Long) -> Unit,
    onLessonsClick: (classId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            TeacherFab(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                onClick = onAddSchoolClassClick,
            )
        },
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
                    leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "") },
                    label = { Text("Uczniowie ($studentCount)") }
                )

                TeacherChip(
                    modifier = chipModifier,
                    onClick = onLessonsClick,
                    leadingIcon = { Icon(Icons.Default.List, contentDescription = "") },
                    label = { Text("Zajęcia ($lessonCount)") },
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
            text = "Nie istnieje jeszcze żadna klasa",
            style = MaterialTheme.typography.displayLarge,
        )
        Icon(
            modifier = Modifier.weight(1f),
            imageVector = Icons.Default.Warning,
            contentDescription = null,
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
            )
        }
    }
}