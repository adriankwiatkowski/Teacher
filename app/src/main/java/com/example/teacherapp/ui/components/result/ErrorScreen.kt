package com.example.teacherapp.ui.components.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    label: String = "",
) {
    ErrorScreen(
        modifier = modifier,
        label = { Text(label) },
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
        Text(
            text = "Wystąpił nieoczekiwany błąd",
            style = MaterialTheme.typography.displayMedium,
        )
        label()
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    TeacherAppTheme {
        Surface {
            ErrorScreen()
        }
    }
}