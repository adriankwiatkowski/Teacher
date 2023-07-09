package com.example.teacherapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.ui.components.TeacherBottomBar
import com.example.teacherapp.ui.components.TeacherFab
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.nav.TeacherBottomNavScreen
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.nav.TeacherNavGraph
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.theme.TeacherAppTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    title: String,
    setTitle: (String) -> Unit,
    menuItems: List<ActionMenuItem>,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    fabAction: FabAction?,
    addFabAction: (fabAction: FabAction) -> Unit,
    removeFabAction: (fabAction: FabAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val navActions: TeacherNavigationActions =
        remember(navController) { TeacherNavigationActions(navController) }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val screens = remember { TeacherBottomNavScreen.values().toList() }
//    val selected: TeacherBottomNavScreen? = screens.firstOrNull { screen ->
//        currentDestination?.hierarchy?.any { it.route == screen.route } ?: false
//    }

    val selected: TeacherBottomNavScreen? = remember(currentDestination) {
        val route = currentDestination?.route ?: return@remember null
        when (route) {
            TeacherDestinations.SCHEDULE_ROUTE -> TeacherBottomNavScreen.Calendar
            TeacherDestinations.SCHOOL_CLASSES_ROUTE,
            TeacherDestinations.SCHOOL_CLASS_ROUTE,
            TeacherDestinations.STUDENT_ROUTE -> TeacherBottomNavScreen.SchoolClasses
            TeacherDestinations.SETTINGS_ROUTE -> TeacherBottomNavScreen.Settings
            else -> null
        }
    }

    val showBottomBar by remember(currentDestination) {
        derivedStateOf {
            // selected != null
            val route = currentDestination?.route ?: return@derivedStateOf true
            when (route) {
                TeacherDestinations.SCHEDULE_ROUTE,
                TeacherDestinations.SCHOOL_CLASSES_ROUTE,
                TeacherDestinations.STUDENT_ROUTE,
                TeacherDestinations.SETTINGS_ROUTE,
                TeacherDestinations.SCHOOL_CLASS_ROUTE -> true
                else -> false
            }
        }
    }
    val showTopBar by remember(currentDestination) {
        derivedStateOf {
            val route = currentDestination?.route ?: return@derivedStateOf true
            when (route) {
                TeacherDestinations.SCHEDULE_ROUTE,
                TeacherDestinations.SCHOOL_CLASSES_ROUTE,
                TeacherDestinations.SETTINGS_ROUTE -> false
                else -> true
            }
        }
    }

    val showSnackbar: ((message: String) -> Unit) = remember(scaffoldState) {
        { message ->
            coroutineScope.launch {
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
                onNavigationIconClick = {
                    navController.navigateUp()
                },
                visible = showTopBar,
            )
        },
        floatingActionButton = {
            TeacherFab(fabAction = fabAction)
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            TeacherBottomBar(
                screens = screens,
                selected = selected,
                visible = showBottomBar,
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
            setTitle = setTitle,
            showSnackbar = showSnackbar,
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
            MainScreen(
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
            MainScreen(
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