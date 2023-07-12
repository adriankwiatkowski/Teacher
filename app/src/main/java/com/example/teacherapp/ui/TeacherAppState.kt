package com.example.teacherapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.teacherapp.ui.nav.TeacherBottomNavScreen
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.nav.rememberNavActions
import kotlinx.coroutines.CoroutineScope

@Composable
fun rememberTeacherAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navActions: TeacherNavigationActions = rememberNavActions(navController = navController),
): TeacherAppState {
    return remember(navController) {
        TeacherAppState(
            navController = navController,
            coroutineScope = coroutineScope,
            navActions = navActions,
        )
    }
}

@Stable
class TeacherAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val navActions: TeacherNavigationActions,
) {

    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    // Consider adding cache.
    val selectedBottomNavigationItem: TeacherBottomNavScreen?
        @Composable get() = when (currentDestination?.route) {
            TeacherDestinations.SCHEDULE_ROUTE -> TeacherBottomNavScreen.Calendar
            TeacherDestinations.SCHOOL_CLASSES_ROUTE,
            TeacherDestinations.SCHOOL_CLASS_ROUTE,
            TeacherDestinations.STUDENT_ROUTE -> TeacherBottomNavScreen.SchoolClasses
            TeacherDestinations.SETTINGS_ROUTE -> TeacherBottomNavScreen.Settings
            else -> null
        }

    val shouldShowBottomBar: Boolean
        @Composable get() = when (currentDestination?.route) {
            TeacherDestinations.SCHEDULE_ROUTE,
            TeacherDestinations.SCHOOL_CLASSES_ROUTE,
            TeacherDestinations.STUDENT_ROUTE,
            TeacherDestinations.SETTINGS_ROUTE,
            TeacherDestinations.SCHOOL_CLASS_ROUTE -> true
            else -> false
        }

    val shouldShowTopBar: Boolean
        @Composable get() = when (currentDestination?.route) {
            TeacherDestinations.SCHEDULE_ROUTE,
            TeacherDestinations.SCHOOL_CLASSES_ROUTE,
            TeacherDestinations.SETTINGS_ROUTE -> false
            else -> true
        }
}