package com.example.teacherapp.ui.screens.student

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.entities.BasicStudent
import com.example.teacherapp.ui.components.student.StudentItem
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun StudentsScreen(
    students: List<BasicStudent>,
    onStudentClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(students, key = { it.id }) { student ->
            StudentItem(
                modifier = Modifier.fillMaxWidth(),
                name = student.name,
                surname = student.surname,
                email = student.email,
                phone = student.phone,
                onClick = { onStudentClick(student.id) }
            )
        }
    }
}

@Preview
@Composable
private fun StudentsScreenPreview() {
    TeacherAppTheme {
        Surface {
            val students = listOf(
                BasicStudent(
                    id = 1,
                    orderInClass = 1,
                    classId = 1,
                    name = "Janveravervrearrvrevaervaeriovjoerivjoaeroitrearaertertreat",
                    surname = "Kowalski",
                    email = "jan.kowalski@domain.com",
                    phone = "123456789",
                ),
                BasicStudent(
                    id = 2,
                    orderInClass = 2,
                    classId = 1,
                    name = "Jan Amadeusz",
                    surname = "Kowalski",
                    email = "janamadeusz.kowalski@domain.com",
                    phone = "987654321",
                ),
            )
            StudentsScreen(
                students = students,
                onStudentClick = {},
            )
        }
    }
}