package com.example.teacherapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

        TeacherFab(
            imageVector = fabAction.imageVector,
            contentDescription = fabAction.contentDescription,
            onClick = fabAction.onClick,
        )
    }
}

@Composable
fun TeacherFab(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(modifier = modifier, onClick = onClick) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}