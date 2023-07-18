package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.teacherapp.ui.TeacherAppState
import com.example.teacherapp.ui.nav.graphs.lesson.lessonGraph
import com.example.teacherapp.ui.nav.graphs.schedule.scheduleGraph
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation
import com.example.teacherapp.ui.nav.graphs.schoolclass.schoolClassGraph
import com.example.teacherapp.ui.nav.graphs.schoolyear.schoolYearGraph
import com.example.teacherapp.ui.nav.graphs.settings.settingsGraph
import com.example.teacherapp.ui.nav.graphs.student.studentGraph

@Composable
fun TeacherNavGraph(
    appState: TeacherAppState,
    onShowSnackbar: (message: String) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = SchoolClassNavigation.schoolClassGraphRoute,
) {
    val navController = appState.navController

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        scheduleGraph()

        schoolClassGraph(navController = navController, onShowSnackbar = onShowSnackbar)
        schoolYearGraph(navController = navController, onShowSnackbar = onShowSnackbar)
        studentGraph(navController = navController, onShowSnackbar = onShowSnackbar)
        lessonGraph(navController = navController, onShowSnackbar = onShowSnackbar)

        settingsGraph()
    }
}