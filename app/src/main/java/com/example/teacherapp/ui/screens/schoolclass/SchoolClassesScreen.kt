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
import androidx.compose.ui.unit.dp
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.data.models.entities.BasicSchoolClass
import com.example.teacherapp.ui.components.form.TeacherChip
import com.example.teacherapp.ui.screens.paramproviders.BasicSchoolClassesPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.warning

@Composable
fun SchoolClassesScreen(
    classes: List<BasicSchoolClass>,
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

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(classes, key = { it.id }) { schoolClass ->
            ClassItem(
                name = schoolClass.name,
                studentCount = schoolClass.studentCount,
                onClick = { onClassClick(schoolClass.id) },
                onStudentsClick = { onStudentsClick(schoolClass.id) },
                onLessonsClick = { onLessonsClick(schoolClass.id) },
            )
        }

        if (classes.isEmpty()) {
            item {
                EmptyClasses(Modifier.fillMaxWidth())
            }
        }
    }
}

//@Composable
//fun SchoolClassesScreen(
//    classes: Map<SchoolYear, ExpandableBasicSchoolClasses>,
//    onAddSchoolClassClick: () -> Unit,
//    onClassClick: (id: Long) -> Unit,
//    onStudentsClick: (classId: Long) -> Unit,
//    onLessonsClick: (classId: Long) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    LazyColumn(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        contentPadding = PaddingValues(8.dp),
//    ) {
//        if (classes.isNotEmpty()) {
//            for ((year, expandableClasses) in classes) {
//                expandableLazyItem(
//                    label = year.name,
//                    expanded = expandableClasses.expanded,
//                ) { contentPadding ->
//                    items(expandableClasses.schoolClasses, key = { it.id }) { schoolClass ->
//                        ClassItem(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(contentPadding),
//                            name = schoolClass.name,
//                            studentCount = schoolClass.studentCount,
//                            onClick = { onClassClick(schoolClass.id) },
//                            onStudentsClick = { onStudentsClick(schoolClass.id) },
//                            onLessonsClick = { onLessonsClick(schoolClass.id) },
//                        )
//                    }
//                }
//            }
//        } else {
//            item {
//                EmptyClasses(Modifier.fillMaxWidth())
//            }
//        }
//
//        item {
//            TeacherOutlinedButton(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = { onAddSchoolClassClick() },
//            ) {
//                Text(text = "Dodaj klasę")
//            }
//        }
//    }
//}

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
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Klasa: $name")

            val chipModifier = Modifier
                .weight(1f)
                .padding(8.dp)

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
                    Text("Zajęcia")
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
                classes = basicSchoolClasses,
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