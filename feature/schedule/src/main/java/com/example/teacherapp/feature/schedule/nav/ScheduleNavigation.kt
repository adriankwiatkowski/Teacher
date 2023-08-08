package com.example.teacherapp.feature.schedule.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation.lessonIdArg
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation.lessonPickerRoute
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation.scheduleRoute

private const val scheduleScreen = "schedule"
private const val eventFormScreen = "event-form"
private const val lessonPickerScreen = "lesson-picker"

object ScheduleNavigation {
    internal const val lessonIdArg = "lesson-id"

    const val scheduleRoute = scheduleScreen
    const val lessonPickerRoute = lessonPickerScreen
}

private const val eventFormRoute = "$eventFormScreen/{$lessonIdArg}"

fun NavController.navigateToScheduleRoute(navOptions: NavOptions? = null) {
    this.navigate(scheduleScreen, navOptions)
}

private fun NavController.navigateToLessonPickerRoute(navOptions: NavOptions? = null) {
    this.navigate(lessonPickerScreen, navOptions)
}

private fun NavController.navigateToEventFormRoute(
    lessonId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$eventFormScreen/$lessonId", navOptions)
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
            onLessonClick = navController::navigateToEventFormRoute,
        )
    }
    composable(
        eventFormRoute,
        arguments = listOf(
            navArgument(lessonIdArg) {
                type = NavType.LongType
            },
        ),
    ) {
        EventFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onSave = { navController.popBackStack(scheduleRoute, inclusive = false) },
            onShowSnackbar = onShowSnackbar,
        )
    }
}