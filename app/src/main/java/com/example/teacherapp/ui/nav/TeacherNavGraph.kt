package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.teacherapp.ui.TeacherAppState
import com.example.teacherapp.ui.nav.graphs.addScheduleGraph
import com.example.teacherapp.ui.nav.graphs.addSettingsGraph
import com.example.teacherapp.ui.nav.graphs.lesson.lessonGraph
import com.example.teacherapp.ui.nav.graphs.schoolclass.SchoolClassNavigation
import com.example.teacherapp.ui.nav.graphs.schoolclass.schoolClassGraph
import com.example.teacherapp.ui.nav.graphs.schoolyear.schoolYearGraph
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
        addScheduleGraph()

        schoolClassGraph(navController = navController, onShowSnackbar = onShowSnackbar)
        schoolYearGraph(navController = navController)
        studentGraph(navController = navController, onShowSnackbar = onShowSnackbar)
        lessonGraph(navController = navController, onShowSnackbar = onShowSnackbar)

        addSettingsGraph()
    }
}