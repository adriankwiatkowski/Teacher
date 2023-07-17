package com.example.teacherapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
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

    NavigationBar(
        modifier = modifier,
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(screen.title) },
                selected = selected == screen,
                onClick = { onClick(screen) },
            )
        }
    }
}