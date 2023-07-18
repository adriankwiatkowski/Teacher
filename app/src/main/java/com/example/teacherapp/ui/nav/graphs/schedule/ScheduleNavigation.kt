package com.example.teacherapp.ui.nav.graphs.schedule

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.ui.nav.graphs.schedule.ScheduleNavigation.scheduleRoute
import com.example.teacherapp.ui.nav.graphs.schedule.route.ScheduleRoute

private const val scheduleScreen = "schedule"

internal object ScheduleNavigation {
    internal const val scheduleRoute = scheduleScreen
}

fun NavController.navigateToScheduleRoute(navOptions: NavOptions? = null) {
    this.navigate(scheduleScreen, navOptions)
}

fun NavGraphBuilder.scheduleGraph() {
    composable(scheduleRoute) {
        ScheduleRoute()
    }
}