package com.example.teacher.core.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun TeacherDiscardDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.ui_discard_title),
    text: String = stringResource(R.string.ui_discard_message),
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TeacherTextButton(
                label = stringResource(R.string.ui_discard),
                onClick = onConfirmClick,
            )
        },
        dismissButton = {
            TeacherTextButton(
                label = stringResource(R.string.ui_cancel),
                onClick = onDismissRequest,
            )
        },
    )
}

@Preview
@Composable
private fun TeacherDiscardDialogPreview() {
    TeacherTheme {
        Surface {
            TeacherDiscardDialog(
                onDismissRequest = {},
                onConfirmClick = {},
            )
        }
    }
}