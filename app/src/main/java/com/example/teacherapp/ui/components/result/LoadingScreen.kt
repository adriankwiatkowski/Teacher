package com.example.teacherapp.ui.components.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    label: String = "Wczytywanie...",
) {
    LoadingScreen(
        modifier = modifier,
        label = { Text(label) },
    )
}

@Composable
fun LoadingScreen(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        label()
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    TeacherAppTheme {
        Surface {
            LoadingScreen()
        }
    }
}