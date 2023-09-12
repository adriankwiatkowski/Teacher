package com.example.teacher.feature.schoolclass.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
internal fun LessonItem(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier.clickable(onClick = onClick),
        leadingContent = {
            val icon = TeacherIcons.subject()
            Icon(
                imageVector = icon.icon,
                contentDescription = null,
            )
        },
        headlineContent = { Text(name) },
    )
}

@Preview
@Composable
private fun LessonItemPreview() {
    TeacherTheme {
        Surface {
            LessonItem(
                name = "Matematyka",
                onClick = {},
            )
        }
    }
}