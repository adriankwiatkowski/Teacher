package com.example.teacher.core.ui.component.tab

import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
internal fun TeacherTab(
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
    TeacherTheme {
        Surface {
            TeacherTab(text = "Tab", selected = true, onClick = {})
        }
    }
}

@Preview
@Composable
private fun TeacherTabPreview() {
    TeacherTheme {
        Surface {
            TeacherTab(text = "Tab", selected = false, onClick = {})
        }
    }
}