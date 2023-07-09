package com.example.teacherapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.teacherapp.ui.nav.TeacherBottomNavScreen
import com.example.teacherapp.ui.nav.TeacherNavGraph
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navActions: TeacherNavigationActions =
        remember(navController) { TeacherNavigationActions(navController) }
    val screens = remember { TeacherBottomNavScreen.values().toList() }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selected: TeacherBottomNavScreen? = screens.firstOrNull { screen ->
        currentDestination?.hierarchy?.any { it.route == screen.route } ?: false
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomBar(
                screens = screens,
                selected = selected,
                onClick = { screen ->
                    when (screen) {
                        TeacherBottomNavScreen.Calendar -> navActions.navigateToCalendarRoute()
                        TeacherBottomNavScreen.SchoolClasses -> navActions.navigateToSchoolClassesRoute()
                        TeacherBottomNavScreen.Settings -> navActions.navigateToSettingsRoute()
                    }
                },
            )
        }
    ) { innerPadding ->
        TeacherNavGraph(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            navActions = navActions,
        )
    }
}

@Composable
private fun BottomBar(
    screens: List<TeacherBottomNavScreen>,
    selected: TeacherBottomNavScreen?,
    onClick: (screen: TeacherBottomNavScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = selected != null,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        BottomNavigation(modifier = modifier) {
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
}

@Preview
@Composable
private fun MainScreenPreview() {
    TeacherAppTheme {
        Surface {
            MainScreen()
        }
    }
}