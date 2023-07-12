package com.example.teacherapp.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.data.models.entities.Student
import com.example.teacherapp.ui.components.resource.ResourceContent
import com.example.teacherapp.ui.screens.paramproviders.StudentPreviewParameterProvider
import com.example.teacherapp.ui.screens.student.components.grades
import com.example.teacherapp.ui.screens.student.components.notes
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun StudentDetailScreen(
    studentResource: Resource<Student>,
    studentNotesResource: Resource<List<BasicStudentNote>>,
    onEmailClick: (email: String) -> Unit,
    onPhoneClick: (phone: String) -> Unit,
    onGradeClick: () -> Unit,
    onAddGradeClick: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    isGradesExpanded: MutableState<Boolean>,
    isNotesExpanded: MutableState<Boolean>,
    isStudentDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    ResourceContent(
        modifier = modifier,
        resource = studentResource,
        isDeleted = isStudentDeleted,
        deletedMessage = "Usunięto pomyślnie dane ucznia."
    ) { student ->
        MainScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.small),
            studentName = student.fullName,
            email = student.email,
            onEmailClick = {
                val email = student.email
                if (!email.isNullOrEmpty()) {
                    onEmailClick(email)
                }
            },
            phone = student.phone,
            onPhoneClick = {
                val phone = student.phone
                if (!phone.isNullOrEmpty()) {
                    onPhoneClick(phone)
                }
            },
            isGradesExpanded = isGradesExpanded.value,
            toggleGradesExpanded = { isGradesExpanded.value = !isGradesExpanded.value },
            onGradeClick = onGradeClick,
            onAddGradeClick = onAddGradeClick,
            studentNotes = (studentNotesResource as? Resource.Success)?.data ?: emptyList(),
            isNotesExpanded = isNotesExpanded.value,
            toggleNotesExpanded = { isNotesExpanded.value = !isNotesExpanded.value },
            onNoteClick = onNoteClick,
            onAddNoteClick = onAddNoteClick,
        )
    }
}

@Composable
private fun MainScreen(
    studentName: String,
    email: String?,
    onEmailClick: () -> Unit,
    phone: String?,
    onPhoneClick: () -> Unit,
    isGradesExpanded: Boolean,
    toggleGradesExpanded: () -> Unit,
    onGradeClick: () -> Unit,
    onAddGradeClick: () -> Unit,
    studentNotes: List<BasicStudentNote>,
    isNotesExpanded: Boolean,
    toggleNotesExpanded: () -> Unit,
    onNoteClick: (noteId: Long) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        item {
            Text(studentName)
        }
        item {
            CopyableText(label = "Email", text = email, onClick = onEmailClick)
        }
        item {
            CopyableText(label = "Telefon", text = phone, onClick = onPhoneClick)
        }

        grades(
            expanded = isGradesExpanded,
            toggleExpanded = toggleGradesExpanded,
            onGradeClick = onGradeClick,
            onAddGradeClick = onAddGradeClick,
        )

        notes(
            studentNotes = studentNotes,
            expanded = isNotesExpanded,
            toggleExpanded = toggleNotesExpanded,
            onNoteClick = onNoteClick,
            onAddNoteClick = onAddNoteClick,
        )
    }
}

@Composable
private fun CopyableText(
    label: String,
    text: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (text == null) {
        return
    }

    Row(
        modifier = modifier
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text("$label: $text")
        Icon(Icons.Default.CopyAll, contentDescription = null)
    }
}

@Preview
@Composable
private fun StudentDetailScreenPreview(
    @PreviewParameter(
        StudentPreviewParameterProvider::class,
        limit = 1,
    ) student: Student,
) {
    val isGradesExpanded = remember { mutableStateOf(true) }
    val isNotesExpanded = remember { mutableStateOf(true) }

    TeacherAppTheme {
        Surface {
            StudentDetailScreen(
                studentResource = Resource.Success(student),
                studentNotesResource = Resource.Loading,
                onEmailClick = {},
                onPhoneClick = {},
                onGradeClick = {},
                onAddGradeClick = {},
                onNoteClick = {},
                onAddNoteClick = {},
                isGradesExpanded = isGradesExpanded,
                isNotesExpanded = isNotesExpanded,
                isStudentDeleted = false,
            )
        }
    }
}

@Preview
@Composable
private fun StudentDetailScreenDeletedPreview() {
    val isGradesExpanded = remember { mutableStateOf(false) }
    val isNotesExpanded = remember { mutableStateOf(false) }

    TeacherAppTheme {
        Surface {
            StudentDetailScreen(
                studentResource = Resource.Loading,
                studentNotesResource = Resource.Loading,
                onEmailClick = {},
                onPhoneClick = {},
                onGradeClick = {},
                onAddGradeClick = {},
                onNoteClick = {},
                onAddNoteClick = {},
                isGradesExpanded = isGradesExpanded,
                isNotesExpanded = isNotesExpanded,
                isStudentDeleted = true,
            )
        }
    }
}