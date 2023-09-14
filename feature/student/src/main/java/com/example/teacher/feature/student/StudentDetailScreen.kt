package com.example.teacher.feature.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.paramprovider.StudentPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@Composable
internal fun StudentDetailScreen(
    snackbarHostState: SnackbarHostState,
    student: Student,
    onEmailClick: () -> Unit,
    onPhoneClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        MainScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            studentName = student.fullName,
            registerNumber = student.registerNumber,
            email = student.email,
            onEmailClick = {
                val email = student.email
                if (!email.isNullOrEmpty()) {
                    onEmailClick()
                }
            },
            phone = student.phone,
            onPhoneClick = {
                val phone = student.phone
                if (!phone.isNullOrEmpty()) {
                    onPhoneClick()
                }
            },
        )
    }
}

@Composable
private fun MainScreen(
    studentName: String,
    registerNumber: Long,
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
        Spacer(Modifier.padding(MaterialTheme.spacing.small))

        Text(text = stringResource(R.string.student_register_number_with_data, registerNumber))
        Spacer(Modifier.padding(MaterialTheme.spacing.small))

        TextWithAction(
            label = stringResource(R.string.student_email),
            text = email,
            icon = TeacherIcons.email(),
            onClick = onEmailClick
        )
        TextWithAction(
            label = stringResource(R.string.student_phone),
            text = phone,
            icon = TeacherIcons.phone(),
            onClick = onPhoneClick
        )
    }
}

@Composable
private fun TextWithAction(
    label: String,
    text: String?,
    icon: TeacherIcon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (text.isNullOrBlank()) {
        return
    }

    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .minimumInteractiveComponentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
    ) {
        Text(text = "$label:", style = MaterialTheme.typography.labelMedium)
        Text(text = text, style = MaterialTheme.typography.bodyLarge)

        Icon(imageVector = icon.icon, contentDescription = null)
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
    TeacherTheme {
        Surface {
            StudentDetailScreen(
                snackbarHostState = remember { SnackbarHostState() },
                student = student,
                onEmailClick = {},
                onPhoneClick = {},
            )
        }
    }
}