package com.example.teacherapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.component.TeacherNavigationBar
import com.example.teacherapp.core.ui.component.TeacherNavigationBarItem
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.nav.TeacherBottomNavScreen
import com.example.teacherapp.ui.nav.TeacherNavGraph
import kotlinx.coroutines.launch

@Composable
fun TeacherApp(
    modifier: Modifier = Modifier,
    appState: TeacherAppState = rememberTeacherAppState(),
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val selectedBottomNavItem by appState.selectedBottomNavigationItem.collectAsStateWithLifecycle()
    val shouldShowBottomBar by appState.shouldShowBottomBar.collectAsStateWithLifecycle()
    val bottomNavScreens = remember { TeacherBottomNavScreen.values().toList() }

    val onShowSnackbar: ((message: String) -> Unit) = remember(snackbarHostState) {
        { message ->
            appState.coroutineScope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
//            TeacherNavigationBar(
//                screens = bottomNavScreens,
//                selected = selectedBottomNavItem,
//                visible = shouldShowBottomBar,
//                onClick = { screen ->
//                    val navActions = appState.navActions
//                    when (screen) {
//                        TeacherBottomNavScreen.Schedule -> navActions.navigateToScheduleRoute()
//                        TeacherBottomNavScreen.SchoolClasses -> navActions.navigateToSchoolClassesRoute()
//                        TeacherBottomNavScreen.Settings -> navActions.navigateToSettingsRoute()
//                    }
//                },
//            )
            TeacherNavigationBar(visible = shouldShowBottomBar) {
                for (screen in bottomNavScreens) {
                    TeacherNavigationBarItem(
                        selected = screen == selectedBottomNavItem,
                        onClick = {
                            val navActions = appState.navActions
                            when (screen) {
                                TeacherBottomNavScreen.Schedule -> {
                                    navActions.navigateToScheduleRoute()
                                }
                                TeacherBottomNavScreen.SchoolClasses -> {
                                    navActions.navigateToSchoolClassesRoute()
                                }
                                TeacherBottomNavScreen.Settings -> {
                                    navActions.navigateToSettingsRoute()
                                }
                            }
                        },
                        icon = screen.icon,
                        iconContentDescription = null,
                        label = screen.title,
                    )
                }
            }
        }
    ) { innerPadding ->
        TeacherNavGraph(
            modifier = Modifier.padding(innerPadding),
            appState = appState,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    TeacherAppTheme {
        Surface {
            TeacherApp()
        }
    }
}