package com.example.teacher.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.model.TeacherAction
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun TeacherFab(
    action: TeacherAction,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
//        LargeFloatingActionButton(
        FloatingActionButton(
            modifier = modifier,
            onClick = action.onClick,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ) {
            val contentDescription = action.contentDescription?.let { stringResource(it) }
            Icon(imageVector = action.imageVector, contentDescription = contentDescription)
        }
    }
}

@Composable
fun TeacherExtendedFab(
    action: TeacherAction,
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
            onClick = action.onClick,
            text = { Text(stringResource(action.text)) },
            icon = {
                val contentDescription = action.contentDescription?.let { stringResource(it) }
                Icon(imageVector = action.imageVector, contentDescription = contentDescription)
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}

@Preview
@Composable
private fun TeacherFabPreview() {
    TeacherTheme {
        Surface {
            TeacherFab(action = TeacherActions.add(onClick = {}))
        }
    }
}

@Preview
@Composable
private fun TeacherExtendedFabPreview() {
    TeacherTheme {
        Surface {
            TeacherExtendedFab(action = TeacherActions.add(onClick = {}))
        }
    }
}