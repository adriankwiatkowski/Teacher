package com.example.teacherapp.ui.components.tab

import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun TeacherTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Tab(
        modifier = modifier,
        text = { Text(text) },
        selected = selected,
        onClick = onClick,
    )
}

@Preview
@Composable
private fun TeacherTabSelectedPreview() {
    TeacherAppTheme {
        Surface {
            TeacherTab(text = "Tab", selected = true, onClick = {})
        }
    }
}

@Preview
@Composable
private fun TeacherTabPreview() {
    TeacherAppTheme {
        Surface {
            TeacherTab(text = "Tab", selected = false, onClick = {})
        }
    }
}