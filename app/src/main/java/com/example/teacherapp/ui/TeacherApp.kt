package com.example.teacherapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.ui.components.TeacherBottomNav
import com.example.teacherapp.ui.components.TeacherFab
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.nav.TeacherBottomNavScreen
import com.example.teacherapp.ui.nav.TeacherNavGraph
import com.example.teacherapp.ui.theme.TeacherAppTheme
import kotlinx.coroutines.launch

@Composable
fun TeacherApp(
    title: String,
    setTitle: (String) -> Unit,
    menuItems: List<ActionMenuItem>,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    fabAction: FabAction?,
    addFabAction: (fabAction: FabAction) -> Unit,
    removeFabAction: (fabAction: FabAction) -> Unit,
    modifier: Modifier = Modifier,
    appState: TeacherAppState = rememberTeacherAppState(),
) {
    val scaffoldState = rememberScaffoldState()

    val selectedBottomNavItem = appState.selectedBottomNavigationItem
    val bottomNavScreens = remember { TeacherBottomNavScreen.values().toList() }

    val onShowSnackbar: ((message: String) -> Unit) = remember(scaffoldState) {
        { message ->
            appState.coroutineScope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            TeacherTopBar(
                title = title,
                menuItems = menuItems,
                showNavigationIcon = true,
                onNavigationIconClick = appState.navController::navigateUp,
                visible = appState.shouldShowTopBar,
            )
        },
        floatingActionButton = {
            TeacherFab(fabAction = fabAction)
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            TeacherBottomNav(
                screens = bottomNavScreens,
                selected = selectedBottomNavItem,
                visible = appState.shouldShowBottomBar,
                onClick = { screen ->
                    val navActions = appState.navActions
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
            appState = appState,
            setTitle = setTitle,
            onShowSnackbar = onShowSnackbar,
            addActionMenuItems = addActionMenuItems,
            removeActionMenuItems = removeActionMenuItems,
            addFabAction = addFabAction,
            removeFabAction = removeFabAction,
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    TeacherAppTheme {
        Surface {
            TeacherApp(
                title = "",
                setTitle = {},
                menuItems = listOf(
                    ActionMenuItem(
                        name = "Name",
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        onClick = {},
                    ),
                ),
                addActionMenuItems = {},
                removeActionMenuItems = {},
                fabAction = FabAction(
                    onClick = {},
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                ),
                addFabAction = {},
                removeFabAction = {},
            )
        }
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenDarkPreview() {
    TeacherAppTheme {
        Surface {
            TeacherApp(
                title = "",
                setTitle = {},
                menuItems = listOf(
                    ActionMenuItem(
                        name = "Name",
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        onClick = {},
                    ),
                ),
                addActionMenuItems = {},
                removeActionMenuItems = {},
                fabAction = FabAction(
                    onClick = {},
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = null,
                ),
                addFabAction = {},
                removeFabAction = {},
            )
        }
    }
}