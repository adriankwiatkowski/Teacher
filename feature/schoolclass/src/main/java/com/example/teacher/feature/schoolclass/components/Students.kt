package com.example.teacher.feature.schoolclass.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.ui.component.TeacherIconButton
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.component.expandablelist.expandableItems
import com.example.teacher.core.ui.paramprovider.BasicStudentsPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.feature.schoolclass.R

internal fun LazyListScope.students(
    label: String,
    students: List<BasicStudent>,
    onStudentClick: (Long) -> Unit,
    onAddStudentClick: () -> Unit,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    expandableItems(
        modifier = modifier,
        label = label,
        expanded = expanded,
        items = students,
        key = { student -> "student-${student.id}" },
        additionalIcon = { TeacherIconButton(TeacherActions.add(onClick = onAddStudentClick)) },
    ) { contentPadding, student ->
        StudentItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding),
            name = student.name,
            surname = student.surname,
            email = student.email,
            phone = student.phone,
            onClick = { onStudentClick(student.id) },
        )
    }

    if (students.isEmpty()) {
        item {
            TeacherLargeText(stringResource(R.string.school_class_no_students_in_class))
        }
    }
}

@Preview
@Composable
private fun StudentsPreview(
    @PreviewParameter(BasicStudentsPreviewParameterProvider::class) students: List<BasicStudent>,
) {
    TeacherTheme {
        Surface {
            val expanded = remember { mutableStateOf(false) }
            val label = stringResource(R.string.school_class_students_with_data, students.size)

            LazyColumn {
                students(
                    label = label,
                    students = students,
                    onStudentClick = {},
                    onAddStudentClick = {},
                    expanded = expanded,
                )
            }
        }
    }
}