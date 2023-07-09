package com.example.teacherapp.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun StudentScreen(
    modifier: Modifier = Modifier,
) {
    MainScreen(
        modifier = modifier,
        schoolClassName = "Klasa 3A",
        studentName = "Jan Kowalski",
        email = "jan.kowalski@email.com",
        onEmailClick = {},
        phone = "123456789",
        onPhoneClick = {},
        onEditClick = {},
        onDeleteClick = {},
    )
}

@Composable
private fun MainScreen(
    schoolClassName: String,
    studentName: String,
    email: String,
    onEmailClick: () -> Unit,
    phone: String,
    onPhoneClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = schoolClassName, style = MaterialTheme.typography.h4)
            Icon(
                modifier = Modifier.clickable(onClick = onEditClick),
                imageVector = Icons.Default.Edit,
                contentDescription = null,
            )
            Icon(
                modifier = Modifier.clickable(onClick = onDeleteClick),
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        }

        Text(studentName)
        CopyableText(text = email, onClick = onEmailClick)
        CopyableText(text = phone, onClick = onPhoneClick)
    }
}

@Composable
private fun CopyableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        Text(text)
        Icon(Icons.Default.CopyAll, contentDescription = null)
    }
}

@Preview
@Composable
private fun StudentScreenPreview() {
    TeacherAppTheme {
        Surface {
            StudentScreen()
        }
    }
}