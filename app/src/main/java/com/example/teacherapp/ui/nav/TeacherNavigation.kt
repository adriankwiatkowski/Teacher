package com.example.teacherapp.ui.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.example.teacherapp.feature.schoolclass.navigateToSchoolClassGraph
import com.example.teacherapp.feature.settings.navigateToSettingsRoute
import com.example.teacherapp.ui.nav.graphs.schedule.navigateToScheduleRoute

enum class TeacherBottomNavScreen(val title: String, val icon: ImageVector) {
    Schedule("Plan zajęć", Icons.Default.Person),
    SchoolClasses("Klasy", Icons.Default.Menu),
    Settings("Ustawienia", Icons.Default.Settings),
}

fun NavController.navigateToScheduleRouteNavigationBar() {
    this.navigateToScheduleRoute(bottomBarNavOptions())
}

fun NavController.navigateToSchoolClassesRouteNavigationBar() {
    this.navigateToSchoolClassGraph(bottomBarNavOptions())
}

fun NavController.navigateToSettingsRouteNavigationBar() {
    this.navigateToSettingsRoute(bottomBarNavOptions())
}

private fun NavController.bottomBarNavOptions(): NavOptions = navOptions {
    setBottomBarNavOptions(this@bottomBarNavOptions.graph)
}

private fun NavOptionsBuilder.setBottomBarNavOptions(graph: NavGraph) {
    // Pop up to the start destination of the graph to
    // avoid building up a large stack of destinations
    // on the back stack as users select items
    popUpTo(graph.findStartDestination().id) {
        saveState = true
    }
    // Avoid multiple copies of the same destination when
    // reselecting the same item
    launchSingleTop = true
    // Restore state when reselecting a previously selected item
    restoreState = true
}