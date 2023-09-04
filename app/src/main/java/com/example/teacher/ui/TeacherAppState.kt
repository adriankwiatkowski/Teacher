package com.example.teacher.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.teacher.feature.grade.nav.GradeNavigation
import com.example.teacher.feature.lesson.nav.LessonNavigation
import com.example.teacher.feature.note.nav.NoteNavigation
import com.example.teacher.feature.schedule.nav.ScheduleNavigation
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
import com.example.teacher.feature.settings.nav.SettingsNavigation
import com.example.teacher.feature.student.nav.StudentNavigation
import com.example.teacher.ui.nav.TeacherBottomNavScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberTeacherAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): TeacherAppState {
    return remember(navController) {
        TeacherAppState(navController = navController, coroutineScope = coroutineScope)
    }
}

@Stable
class TeacherAppState(val navController: NavHostController, val coroutineScope: CoroutineScope) {

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedBottomNavigationItem: StateFlow<TeacherBottomNavScreen?> =
        navController.currentBackStackEntryFlow
            .mapLatest { backStackEntry ->
                when (backStackEntry.destination.route) {
                    in scheduleRoutes -> TeacherBottomNavScreen.Schedule
                    in schoolClassesRoutes -> TeacherBottomNavScreen.SchoolClasses
                    in notesRoutes -> TeacherBottomNavScreen.Notes
                    in settingsRoutes -> TeacherBottomNavScreen.Settings
                    else -> null
                }
            }
            .stateIn(initialValue = null)

    val shouldShowBottomBar: StateFlow<Boolean> = selectedBottomNavigationItem
        .map { it != null }
        .stateIn(initialValue = false)

    private val scheduleRoutes = setOf(ScheduleNavigation.scheduleRoute)

    private val schoolClassesRoutes = setOf(
        SchoolClassNavigation.schoolClassesRoute,
        SchoolClassNavigation.schoolClassRoute,
        StudentNavigation.studentRoute,
        LessonNavigation.lessonRoute,
        GradeNavigation.gradesRoute,
    )

    private val notesRoutes = setOf(NoteNavigation.notesRoute)

    private val settingsRoutes = setOf(SettingsNavigation.settingsRoute)

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )
}