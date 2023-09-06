package com.example.teacher.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.component.TeacherNavigationBar
import com.example.teacher.core.ui.component.TeacherNavigationBarItem
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.ui.nav.TeacherBottomNavScreen
import com.example.teacher.ui.nav.TeacherNavGraph
import com.example.teacher.ui.nav.navigateToNotesRouteNavigationBar
import com.example.teacher.ui.nav.navigateToScheduleRouteNavigationBar
import com.example.teacher.ui.nav.navigateToSchoolClassesRouteNavigationBar
import com.example.teacher.ui.nav.navigateToSettingsRouteNavigationBar
import kotlinx.coroutines.launch

@Composable
fun TeacherApp(
    isAuthenticated: Boolean,
    authenticate: () -> Unit,
    unAuthenticate: () -> Unit,
    isDeviceSecure: Boolean,
    enableAuthentication: Boolean,
    modifier: Modifier = Modifier,
    appState: TeacherAppState = rememberTeacherAppState(),
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val selectedBottomNavItem by appState.selectedBottomNavigationItem.collectAsStateWithLifecycle()
    val shouldShowBottomBar by appState.shouldShowBottomBar.collectAsStateWithLifecycle()
    val bottomNavScreens = remember { TeacherBottomNavScreen.entries }

    val onShowSnackbar: OnShowSnackbar = remember(snackbarHostState) {
        OnShowSnackbar { message ->
            appState.coroutineScope.launch {
                snackbarHostState.showSnackbar(message = context.getString(message))
            }
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            TeacherNavigationBar(visible = shouldShowBottomBar) {
                for (screen in bottomNavScreens) {
                    TeacherNavigationBarItem(
                        selected = screen == selectedBottomNavItem,
                        onClick = {
                            val navController = appState.navController
                            when (screen) {
                                TeacherBottomNavScreen.Schedule -> {
                                    navController.navigateToScheduleRouteNavigationBar()
                                }

                                TeacherBottomNavScreen.SchoolClasses -> {
                                    navController.navigateToSchoolClassesRouteNavigationBar()
                                }

                                TeacherBottomNavScreen.Notes -> {
                                    navController.navigateToNotesRouteNavigationBar()
                                }

                                TeacherBottomNavScreen.Settings -> {
                                    navController.navigateToSettingsRouteNavigationBar()
                                }
                            }
                        },
                        icon = screen.icon.icon,
                        iconContentDescription = stringResource(screen.icon.text),
                        label = stringResource(screen.icon.text),
                    )
                }
            }
        }
    ) { innerPadding ->
        TeacherNavGraph(
            modifier = Modifier.padding(innerPadding),
            appState = appState,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            isAuthenticated = isAuthenticated,
            authenticate = authenticate,
            unAuthenticate = unAuthenticate,
            isDeviceSecure = isDeviceSecure,
            enableAuthentication = enableAuthentication,
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    TeacherTheme {
        Surface {
            TeacherApp(
                isAuthenticated = true,
                authenticate = {},
                unAuthenticate = {},
                isDeviceSecure = true,
                enableAuthentication = true,
            )
        }
    }
}