package com.example.teacherapp.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.ui.screens.paramproviders.StudentPreviewParameterProvider
import com.example.teacherapp.ui.screens.student.components.grades
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun StudentDetailScreen(
    student: Student,
    onEmailClick: (email: String) -> Unit,
    onPhoneClick: (phone: String) -> Unit,
    onGradeClick: () -> Unit,
    onAddGradeClick: () -> Unit,
    isGradesExpanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    MainScreen(
        modifier = modifier
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
    )
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
            .clickable(onClick = onClick)
            .minimumInteractiveComponentSize(),
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

    TeacherAppTheme {
        Surface {
            StudentDetailScreen(
                student = student,
                onEmailClick = {},
                onPhoneClick = {},
                onGradeClick = {},
                onAddGradeClick = {},
                isGradesExpanded = isGradesExpanded,
            )
        }
    }
}