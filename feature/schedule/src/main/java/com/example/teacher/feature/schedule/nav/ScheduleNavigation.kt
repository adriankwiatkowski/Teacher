package com.example.teacher.feature.schedule.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacher.feature.schedule.data.EventFormViewModel
import com.example.teacher.feature.schedule.nav.ScheduleNavigation.lessonIdArg
import com.example.teacher.feature.schedule.nav.ScheduleNavigation.scheduleRoute
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private const val scheduleScreen = "schedule"
private const val eventFormScreen = "event-form"
private const val lessonPickerScreen = "lesson-picker"

object ScheduleNavigation {
    internal const val lessonIdArg = "lesson-id"

    const val scheduleRoute = scheduleScreen
}

private const val eventFormRoute = "$eventFormScreen?$lessonIdArg={$lessonIdArg}"
private const val lessonPickerRoute = lessonPickerScreen

fun NavController.navigateToScheduleRoute(navOptions: NavOptions? = null) {
    this.navigate(scheduleScreen, navOptions)
}

private fun NavController.navigateToEventFormRoute(
    lessonId: Long? = null,
    navOptions: NavOptions? = null,
) {
    val query = if (lessonId != null) "?$lessonIdArg=$lessonId" else ""
    this.navigate("$eventFormScreen$query", navOptions)
}

private fun NavController.navigateToLessonPickerRoute(navOptions: NavOptions? = null) {
    this.navigate(lessonPickerScreen, navOptions)
}

fun NavGraphBuilder.scheduleGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: (message: String) -> Unit,
) {
    composable(scheduleRoute) {
        ScheduleRoute(
            snackbarHostState = snackbarHostState,
            onAddScheduleClick = navController::navigateToEventFormRoute,
        )
    }

    composable(
        eventFormRoute,
        arguments = listOf(
            navArgument(lessonIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) { backStackEntry ->
        val viewModel = hiltViewModel<EventFormViewModel>()
        val scope = rememberCoroutineScope()

        // Workaround for hilt ViewModel SavedStateHandle not receiving updates.
        LaunchedEffect(scope) {
            backStackEntry.savedStateHandle.getStateFlow(lessonIdArg, 0L)
                .onEach { lessonId -> viewModel.setLessonId(lessonId) }
                .launchIn(scope)
        }

        EventFormRoute(
            showNavigationIcon = true,
            snackbarHostState = snackbarHostState,
            onNavBack = navController::popBackStack,
            onSave = { navController.popBackStack(scheduleRoute, inclusive = false) },
            onShowSnackbar = onShowSnackbar,
            onLessonPickerClick = navController::navigateToLessonPickerRoute,
            viewModel = viewModel,
        )
    }

    composable(lessonPickerRoute) {
        LessonPickerRoute(
            showNavigationIcon = true,
            snackbarHostState = snackbarHostState,
            onNavBack = navController::popBackStack,
            onLessonClick = { lessonId ->
                // Set result to previous screen.
                navController.previousBackStackEntry?.savedStateHandle?.set(lessonIdArg, lessonId)
                navController.popBackStack()
            }
        )
    }
}