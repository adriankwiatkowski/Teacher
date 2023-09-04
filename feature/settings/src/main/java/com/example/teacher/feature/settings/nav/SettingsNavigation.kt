package com.example.teacher.feature.settings.nav

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacher.feature.settings.nav.SettingsNavigation.settingsRoute

private const val settingsScreen = "settings"

object SettingsNavigation {
    const val settingsRoute = settingsScreen
}

fun NavController.navigateToSettingsRoute(navOptions: NavOptions? = null) {
    this.navigate(settingsScreen, navOptions)
}

fun NavGraphBuilder.settingsGraph(snackbarHostState: SnackbarHostState) {
    composable(settingsRoute) {
        SettingsRoute(snackbarHostState = snackbarHostState)
    }
}