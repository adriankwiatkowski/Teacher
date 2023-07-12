package com.example.teacherapp.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.teacherapp.ui.nav.TeacherBottomNavScreen

@Composable
fun TeacherBottomNav(
    screens: List<TeacherBottomNavScreen>,
    selected: TeacherBottomNavScreen?,
    visible: Boolean,
    onClick: (screen: TeacherBottomNavScreen) -> Unit,
    modifier: Modifier = Modifier,
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

    BottomNavigation(
        modifier = modifier,
    ) {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = selected == screen,
                onClick = { onClick(screen) },
            )
        }
    }
}