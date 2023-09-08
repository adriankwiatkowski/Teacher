package com.example.teacher.ui.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.feature.grade.nav.GradeNavigation
import com.example.teacher.feature.lesson.nav.LessonNavigation
import com.example.teacher.feature.note.nav.NoteNavigation
import com.example.teacher.feature.note.nav.navigateToNotesRoute
import com.example.teacher.feature.schedule.nav.ScheduleNavigation
import com.example.teacher.feature.schedule.nav.navigateToScheduleRoute
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
import com.example.teacher.feature.schoolclass.nav.navigateToSchoolClassGraph
import com.example.teacher.feature.settings.nav.SettingsNavigation
import com.example.teacher.feature.settings.nav.navigateToSettingsRoute
import com.example.teacher.feature.student.nav.StudentNavigation
import com.example.teacher.ui.nav.TeacherBottomNavScreen.Notes
import com.example.teacher.ui.nav.TeacherBottomNavScreen.Schedule
import com.example.teacher.ui.nav.TeacherBottomNavScreen.SchoolClasses
import com.example.teacher.ui.nav.TeacherBottomNavScreen.Settings

enum class TeacherBottomNavScreen(val icon: TeacherIcon) {
    Schedule(TeacherIcons.schedule()),
    SchoolClasses(TeacherIcons.schoolClasses()),
    Notes(TeacherIcons.notes()),
    Settings(TeacherIcons.settings()),
}

fun routeToActiveNavScreen(route: String?): TeacherBottomNavScreen? = when (route) {
    in scheduleRoutes -> Schedule
    in schoolClassesRoutes -> SchoolClasses
    in notesRoutes -> Notes
    in settingsRoutes -> Settings
    else -> null
}

fun NavController.navigateToScheduleRouteNavigationBar() {
    this.navigateToScheduleRoute(bottomBarNavOptions(isReSelectingScreen(Schedule)))
}

fun NavController.navigateToSchoolClassesRouteNavigationBar() {
    this.navigateToSchoolClassGraph(bottomBarNavOptions(isReSelectingScreen(SchoolClasses)))
}

fun NavController.navigateToNotesRouteNavigationBar() {
    this.navigateToNotesRoute(bottomBarNavOptions(isReSelectingScreen(Notes)))
}

fun NavController.navigateToSettingsRouteNavigationBar() {
    this.navigateToSettingsRoute(bottomBarNavOptions(isReSelectingScreen(Settings)))
}

private fun NavController.bottomBarNavOptions(
    isReSelectingScreen: Boolean
): NavOptions = navOptions {
    setBottomBarNavOptions(
        this@bottomBarNavOptions.graph,
        shouldRestoreState = !isReSelectingScreen,
    )
}

private fun NavOptionsBuilder.setBottomBarNavOptions(
    graph: NavGraph,
    shouldRestoreState: Boolean,
) {
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
    restoreState = shouldRestoreState
}

private fun NavController.isReSelectingScreen(screen: TeacherBottomNavScreen): Boolean =
    routeToActiveNavScreen(this.currentDestination?.route) == screen

private val scheduleRoutes = setOf(ScheduleNavigation.scheduleRoute)

private val schoolClassesRoutes = setOf(
    SchoolClassNavigation.schoolClassesRoute,
    SchoolClassNavigation.schoolClassRoute,
    StudentNavigation.studentRoute,
    LessonNavigation.lessonRoute,
    GradeNavigation.gradesRoute,
)

private val notesRoutes = setOf(NoteNavigation.notesRoute)

private val settingsRoutes = setOf(SettingsNavigation.settingsRoute)