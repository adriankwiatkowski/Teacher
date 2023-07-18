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
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation
import com.example.teacherapp.ui.nav.graphs.schedule.ScheduleNavigation
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation
import com.example.teacherapp.ui.nav.graphs.settings.SettingsNavigation
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation
import com.example.teacherapp.ui.nav.rememberNavActions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedBottomNavigationItem: StateFlow<TeacherBottomNavScreen?> =
        navController.currentBackStackEntryFlow
            .mapLatest { backStackEntry ->
                when (backStackEntry.destination.route) {
                    ScheduleNavigation.scheduleRoute -> TeacherBottomNavScreen.Schedule

                    SchoolClassNavigation.schoolClassesRoute,
                    SchoolClassNavigation.schoolClassRoute,
                    StudentNavigation.studentRoute,
                    LessonNavigation.lessonRoute -> TeacherBottomNavScreen.SchoolClasses

                    SettingsNavigation.settingsRoute -> TeacherBottomNavScreen.Settings
                    else -> null
                }
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = null,
            )

    val shouldShowBottomBar: StateFlow<Boolean> = selectedBottomNavigationItem
        .map { it != null }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false,
        )
}