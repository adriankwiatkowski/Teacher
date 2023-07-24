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
import com.example.teacherapp.feature.schoolclass.SchoolClassNavigation
import com.example.teacherapp.feature.schoolclass.navigateToSchoolClassGraph
import com.example.teacherapp.feature.settings.SettingsNavigation
import com.example.teacherapp.feature.settings.navigateToSettingsRoute
import com.example.teacherapp.ui.nav.graphs.schedule.ScheduleNavigation
import com.example.teacherapp.ui.nav.graphs.schedule.navigateToScheduleRoute

enum class TeacherBottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    Schedule(ScheduleNavigation.scheduleRoute, "Plan zajęć", Icons.Default.Person),
    SchoolClasses(SchoolClassNavigation.schoolClassesRoute, "Klasy", Icons.Default.Menu),
    Settings(SettingsNavigation.settingsRoute, "Ustawienia", Icons.Default.Settings),
}

@Composable
fun rememberNavActions(navController: NavController): TeacherNavigationActions {
    return remember(navController) {
        TeacherNavigationActions(navController = navController)
    }
}

class TeacherNavigationActions(private val navController: NavController) {

    fun navigateToScheduleRoute() {
        navController.navigateToScheduleRoute(navOptions {
            setBottomBarNavOptions()
        })
    }

    fun navigateToSchoolClassesRoute() {
        navController.navigateToSchoolClassGraph(navOptions {
            setBottomBarNavOptions()
        })
    }

    fun navigateToSettingsRoute() {
        navController.navigateToSettingsRoute(navOptions {
            setBottomBarNavOptions()
        })
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