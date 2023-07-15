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
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG
import com.example.teacherapp.ui.nav.TeacherScreens.SCHEDULE_SCREEN
import com.example.teacherapp.ui.nav.TeacherScreens.SCHOOL_CLASSES_SCREEN
import com.example.teacherapp.ui.nav.TeacherScreens.SCHOOL_CLASS_FORM_SCREEN
import com.example.teacherapp.ui.nav.TeacherScreens.SCHOOL_CLASS_SCREEN
import com.example.teacherapp.ui.nav.TeacherScreens.SETTINGS_SCREEN

enum class TeacherBottomNavScreen(val route: String, val title: String, val icon: ImageVector) {
    Calendar(TeacherDestinations.SCHEDULE_ROUTE, "Plan zajęć", Icons.Default.Person),
    SchoolClasses(TeacherDestinations.SCHOOL_CLASSES_ROUTE, "Klasy", Icons.Default.Menu),
    Settings(TeacherDestinations.SETTINGS_ROUTE, "Ustawienia", Icons.Default.Settings),
}

private object TeacherScreens {

    const val SCHEDULE_SCREEN = "schedule"

    const val SCHOOL_CLASSES_SCREEN = "school-classes"
    const val SCHOOL_CLASS_SCREEN = "school-class"
    const val SCHOOL_CLASS_FORM_SCREEN = "school-class-form"

    const val SETTINGS_SCREEN = "settings"
}

object TeacherDestinationsArgs {
    const val SCHOOL_CLASS_ID_ARG = "school-class-id"
}

object TeacherDestinations {

    const val SCHEDULE_ROUTE = SCHEDULE_SCREEN

    const val SCHOOL_CLASSES_ROUTE = SCHOOL_CLASSES_SCREEN
    const val SCHOOL_CLASS_ROUTE = "$SCHOOL_CLASS_SCREEN/{$SCHOOL_CLASS_ID_ARG}"
    const val SCHOOL_CLASS_FORM_ROUTE = SCHOOL_CLASS_FORM_SCREEN

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
        navigateToBottomBar(TeacherDestinations.SCHOOL_CLASSES_ROUTE)
    }

    fun navigateToSchoolClassRoute(schoolClassId: Long) {
        navController.navigate("$SCHOOL_CLASS_SCREEN/$schoolClassId")
    }

    fun navigateToSchoolClassFormRoute() {
        navController.navigate(TeacherDestinations.SCHOOL_CLASS_FORM_ROUTE)
    }

    fun navigateToSettingsRoute() {
        navigateToBottomBar(TeacherDestinations.SETTINGS_ROUTE)
    }

    private fun navigateToBottomBar(route: String) {
        navController.navigate(route) {
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
}