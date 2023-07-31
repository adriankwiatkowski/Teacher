package com.example.teacherapp.feature.schedule.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation.lessonIdArg
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation.lessonPickerRoute
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation.scheduleRoute

private const val scheduleScreen = "schedule"
private const val lessonScheduleFormScreen = "lesson-schedule-form"
private const val lessonPickerScreen = "lesson-picker"

object ScheduleNavigation {
    internal const val lessonIdArg = "lesson-id"

    const val scheduleRoute = scheduleScreen
    const val lessonPickerRoute = lessonPickerScreen
}

private const val lessonScheduleFormRoute = "$lessonScheduleFormScreen/{$lessonIdArg}"

fun NavController.navigateToScheduleRoute(navOptions: NavOptions? = null) {
    this.navigate(scheduleScreen, navOptions)
}

private fun NavController.navigateToLessonPickerRoute(navOptions: NavOptions? = null) {
    this.navigate(lessonPickerScreen, navOptions)
}

private fun NavController.navigateToLessonScheduleFormRoute(
    lessonId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$lessonScheduleFormScreen/$lessonId", navOptions)
}

fun NavGraphBuilder.scheduleGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    composable(scheduleRoute) {
        ScheduleRoute(onAddScheduleClick = navController::navigateToLessonPickerRoute)
    }
    composable(lessonPickerRoute) {
        LessonPickerRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onLessonClick = navController::navigateToLessonScheduleFormRoute,
        )
    }
    composable(lessonScheduleFormRoute) {
        LessonScheduleFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
        )
    }
}