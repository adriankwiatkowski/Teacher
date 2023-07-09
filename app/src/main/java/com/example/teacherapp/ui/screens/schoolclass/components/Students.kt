package com.example.teacherapp.ui.screens.schoolclass.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.teacherapp.data.models.entities.BasicStudent
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.student.StudentItem
import com.example.teacherapp.ui.components.utils.expandableItems
import com.example.teacherapp.ui.screens.paramproviders.BasicStudentsPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme

fun LazyListScope.students(
    students: List<BasicStudent>,
    onStudentClick: (Long) -> Unit,
    onAddStudentClick: () -> Unit,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    expandableItems(
        modifier = modifier,
        label = "Uczniowie (${students.size})",
        expanded = expanded,
        items = students,
        key = { student -> "student-${student.id}" },
    ) { contentPadding, student ->
        Card(Modifier.fillMaxWidth()) {
            StudentItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                name = student.name,
                surname = student.surname,
                email = student.email,
                phone = student.phone,
                onClick = { onStudentClick(student.id) }
            )
        }
    }

    item {
        Card {
            TeacherOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onClick = onAddStudentClick,
            ) {
                Text(text = "Dodaj ucznia")
            }
        }
    }
}

@Preview
@Composable
private fun StudentsPreview(
    @PreviewParameter(BasicStudentsPreviewParameterProvider::class) students: List<BasicStudent>,
) {
    TeacherAppTheme {
        Surface {
            val expanded = remember { mutableStateOf(false) }
            LazyColumn {
                students(
                    students = students,
                    onStudentClick = {},
                    onAddStudentClick = {},
                    expanded = expanded,
                )
            }
        }
    }
}