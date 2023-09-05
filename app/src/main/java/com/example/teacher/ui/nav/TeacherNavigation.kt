package com.example.teacher.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.feature.note.nav.navigateToNotesRoute
import com.example.teacher.feature.schedule.nav.navigateToScheduleRoute
import com.example.teacher.feature.schoolclass.nav.navigateToSchoolClassGraph
import com.example.teacher.feature.settings.nav.navigateToSettingsRoute

enum class TeacherBottomNavScreen(val icon: TeacherIcon) {
    Schedule(TeacherIcons.schedule()),
    SchoolClasses(TeacherIcons.schoolClasses()),
    Notes(TeacherIcons.notes()),
    Settings(TeacherIcons.settings()),
}

fun NavController.navigateToScheduleRouteNavigationBar() {
    this.navigateToScheduleRoute(bottomBarNavOptions())
}

fun NavController.navigateToSchoolClassesRouteNavigationBar() {
    this.navigateToSchoolClassGraph(bottomBarNavOptions())
}

fun NavController.navigateToNotesRouteNavigationBar() {
    this.navigateToNotesRoute(bottomBarNavOptions())
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