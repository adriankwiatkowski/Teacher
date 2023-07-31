package com.example.teacherapp.feature.schedule.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation.scheduleRoute

private const val scheduleScreen = "schedule"
private const val lessonScheduleFormScreen = "lesson-schedule-form"

object ScheduleNavigation {
    const val scheduleRoute = scheduleScreen
}

private const val lessonScheduleFormRoute = lessonScheduleFormScreen

fun NavController.navigateToScheduleRoute(navOptions: NavOptions? = null) {
    this.navigate(scheduleScreen, navOptions)
}

private fun NavController.navigateToLessonScheduleFormRoute(navOptions: NavOptions? = null) {
    this.navigate(lessonScheduleFormScreen, navOptions)
}

fun NavGraphBuilder.scheduleGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    composable(scheduleRoute) {
        ScheduleRoute()
    }
    composable(lessonScheduleFormRoute) {
        LessonScheduleFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
        )
    }
}