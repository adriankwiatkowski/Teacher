package com.example.teacherapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.teacherapp.data.models.FabAction

@Composable
fun TeacherFab(
    fabAction: FabAction?,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = fabAction != null,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        if (fabAction == null) {
            return@AnimatedVisibility
        }

        FloatingActionButton(onClick = fabAction.onClick) {
            Icon(
                imageVector = fabAction.imageVector,
                contentDescription = fabAction.contentDescription,
            )
        }
    }
}