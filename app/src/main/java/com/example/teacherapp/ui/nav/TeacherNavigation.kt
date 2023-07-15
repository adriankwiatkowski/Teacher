package com.example.teacherapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.example.teacherapp.ui.nav.TeacherScreens.SCHEDULE_SCREEN
import com.example.teacherapp.ui.nav.TeacherScreens.SETTINGS_SCREEN
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation
import com.example.teacherapp.ui.nav.graphs.schoolclass.navigateToSchoolClassGraph

enum class TeacherBottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    Calendar(TeacherDestinations.SCHEDULE_ROUTE, "Plan zajęć", Icons.Default.Person),
    SchoolClasses(SchoolClassNavigation.schoolClassesRoute, "Klasy", Icons.Default.Menu),
    Settings(TeacherDestinations.SETTINGS_ROUTE, "Ustawienia", Icons.Default.Settings),
}

private object TeacherScreens {

    const val SCHEDULE_SCREEN = "schedule"

    const val SETTINGS_SCREEN = "settings"
}

object TeacherDestinations {
    const val SCHEDULE_ROUTE = SCHEDULE_SCREEN
    const val SETTINGS_ROUTE = SETTINGS_SCREEN
}

@Composable
fun rememberNavActions(navController: NavController): TeacherNavigationActions {
    return remember(navController) {
        TeacherNavigationActions(navController = navController)
    }
}

class TeacherNavigationActions(private val navController: NavController) {

    fun navigateToCalendarRoute() {
        navigateToBottomBar(TeacherDestinations.SCHEDULE_ROUTE)
    }

    fun navigateToSchoolClassesRoute() {
        navController.navigateToSchoolClassGraph(navOptions {
            setBottomBarNavOptions()
        })
    }

    fun navigateToSettingsRoute() {
        navigateToBottomBar(TeacherDestinations.SETTINGS_ROUTE)
    }

    private fun navigateToBottomBar(route: String) {
        navController.navigate(route) {
            setBottomBarNavOptions()
        }
    }

    private fun NavOptionsBuilder.setBottomBarNavOptions() {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}