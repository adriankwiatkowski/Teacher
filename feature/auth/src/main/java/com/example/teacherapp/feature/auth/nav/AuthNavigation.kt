package com.example.teacherapp.feature.auth.nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.teacherapp.feature.auth.AuthScreen
import com.example.teacherapp.feature.auth.nav.AuthNavigation.authRoute

private const val authScreen = "auth"

object AuthNavigation {
    const val authRoute = authScreen
}

fun NavController.navigateToAuthRoute(navOptions: NavOptions? = null) {
    this.navigate(authRoute, navOptions)
}

fun NavGraphBuilder.authGraph(
    authenticate: () -> Unit,
    isDeviceSecure: Boolean,
) {
    composable(authRoute) {
        AuthScreen(
            authenticate = authenticate,
            isDeviceSecure = isDeviceSecure,
        )
    }
}