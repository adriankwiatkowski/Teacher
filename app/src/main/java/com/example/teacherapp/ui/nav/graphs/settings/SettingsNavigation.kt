package com.example.teacherapp.ui.nav.graphs.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.ui.nav.graphs.settings.SettingsNavigation.settingsRoute
import com.example.teacherapp.ui.nav.graphs.settings.route.SettingsRoute

private const val settingsScreen = "settings"

internal object SettingsNavigation {
    internal const val settingsRoute = settingsScreen
}

fun NavController.navigateToSettingsRoute(navOptions: NavOptions? = null) {
    this.navigate(settingsScreen, navOptions)
}

fun NavGraphBuilder.settingsGraph() {
    composable(settingsRoute) {
        SettingsRoute()
    }
}