package com.example.teacherapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun TeacherFab(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        FloatingActionButton(modifier = modifier, onClick = onClick) {
            Icon(imageVector = imageVector, contentDescription = contentDescription)
        }
    }
}

@Composable
fun TeacherExtendedFab(
    text: String,
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        ExtendedFloatingActionButton(
            modifier = modifier,
            onClick = onClick,
            text = { Text(text) },
            icon = { Icon(imageVector = imageVector, contentDescription = contentDescription) },
        )
    }
}

@Preview
@Composable
private fun TeacherFabPreview() {
    TeacherAppTheme {
        Surface {
            TeacherFab(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun TeacherExtendedFabPreview() {
    TeacherAppTheme {
        Surface {
            TeacherExtendedFab(
                text = "Utwórz",
                imageVector = Icons.Default.Add,
                contentDescription = null,
                onClick = {},
            )
        }
    }
}