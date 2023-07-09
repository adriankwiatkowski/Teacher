package com.example.teacherapp.ui.nav.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.screens.other.ProfileScreen

fun NavGraphBuilder.addScheduleGraph() {
    composable(TeacherDestinations.SCHEDULE_ROUTE) {
        ProfileScreen()
    }
}