package com.example.teacherapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.teacherapp.data.models.ActionMenuItem

// TODO: Add support for scrolling behavior.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTopBar(
    title: String,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    menuItems: List<ActionMenuItem> = emptyList(),
) {
//    AnimatedVisibility(
//        modifier = modifier,
//        visible = visible,
//        enter = fadeIn(),
//        exit = fadeOut(),
//    ) {
//    }
    if (!visible) {
        return
    }

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(title)
        },
        navigationIcon = {
            if (showNavigationIcon) {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        actions = {
            for (item in menuItems) {
                IconButton(onClick = item.onClick) {
                    Icon(
                        imageVector = item.imageVector,
                        contentDescription = item.contentDescription,
                    )
                }
            }
        }
    )
}