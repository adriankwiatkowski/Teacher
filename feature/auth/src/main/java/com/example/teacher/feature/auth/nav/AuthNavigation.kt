package com.example.teacher.feature.auth.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacher.core.ui.util.BackPressHandler
import com.example.teacher.feature.auth.AuthScreen
import com.example.teacher.feature.auth.nav.AuthNavigation.authRoute

private const val authScreen = "auth"

object AuthNavigation {
    const val authRoute = authScreen
}

fun NavController.navigateToAuthRoute(navOptions: NavOptions? = null) {
    this.navigate(authScreen, navOptions)
}

fun NavGraphBuilder.authGraph(authenticate: () -> Unit, isDeviceSecure: Boolean) {
    composable(authRoute) {
        // Don't allow back press when user is authenticating.
        BackPressHandler {
            // Do nothing.
        }

        AuthScreen(authenticate = authenticate, isDeviceSecure = isDeviceSecure)
    }
}