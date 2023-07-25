package com.example.teacherapp.feature.note

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.ui.theme.TeacherAppTheme

@Composable
internal fun NotesScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = "Notatki")
    }
}

@Preview
@Composable
private fun NotesScreenPreview() {
    TeacherAppTheme {
        Surface {
            NotesScreen()
        }
    }
}