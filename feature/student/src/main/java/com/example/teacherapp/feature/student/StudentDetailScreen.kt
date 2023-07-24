package com.example.teacherapp.feature.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.student.paramprovider.StudentPreviewParameterProvider

@Composable
internal fun StudentDetailScreen(
    student: Student,
    onEmailClick: (email: String) -> Unit,
    onPhoneClick: (phone: String) -> Unit,
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
    )
}

@Composable
private fun MainScreen(
    studentName: String,
    email: String?,
    onEmailClick: () -> Unit,
    phone: String?,
    onPhoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
        Text(text = studentName, style = MaterialTheme.typography.titleLarge)
        CopyableText(label = "Email", text = email, onClick = onEmailClick)
        CopyableText(label = "Telefon", text = phone, onClick = onPhoneClick)
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
        Text(text = "$label: $text", style = MaterialTheme.typography.bodyLarge)
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
    TeacherAppTheme {
        Surface {
            StudentDetailScreen(
                student = student,
                onEmailClick = {},
                onPhoneClick = {},
            )
        }
    }
}