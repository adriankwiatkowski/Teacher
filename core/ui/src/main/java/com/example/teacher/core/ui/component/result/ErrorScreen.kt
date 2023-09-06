package com.example.teacher.core.ui.component.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.component.TeacherLargeText
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    label: String = "",
) {
    ErrorScreen(
        modifier = modifier,
        label = { TeacherLargeText(label) },
    )
}

@Composable
fun ErrorScreen(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TeacherLargeText(text = stringResource(R.string.ui_unexpected_error))
        label()
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    TeacherTheme {
        Surface {
            ErrorScreen()
        }
    }
}