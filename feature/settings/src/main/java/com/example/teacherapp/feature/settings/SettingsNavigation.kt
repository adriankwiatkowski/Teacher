package com.example.teacherapp.feature.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.feature.settings.SettingsNavigation.settingsRoute

private const val settingsScreen = "settings"

object SettingsNavigation {
    const val settingsRoute = settingsScreen
}

fun NavController.navigateToSettingsRoute(navOptions: NavOptions? = null) {
    this.navigate(settingsScreen, navOptions)
}

fun NavGraphBuilder.settingsGraph() {
    composable(settingsRoute) {
        SettingsRoute()
    }
}