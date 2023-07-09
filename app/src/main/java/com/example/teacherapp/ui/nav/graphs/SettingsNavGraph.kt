package com.example.teacherapp.ui.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.screens.other.SettingsScreen

fun NavGraphBuilder.addSettingsGraph() {
    composable(TeacherDestinations.SETTINGS_ROUTE) {
        SettingsScreen()
    }
}