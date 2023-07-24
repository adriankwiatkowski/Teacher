package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.teacherapp.feature.schoolclass.SchoolClassNavigation
import com.example.teacherapp.feature.schoolclass.schoolClassGraph
import com.example.teacherapp.feature.schoolyear.navigateToSchoolYearFormRoute
import com.example.teacherapp.feature.schoolyear.schoolYearGraph
import com.example.teacherapp.feature.settings.settingsGraph
import com.example.teacherapp.feature.student.navigateToStudentFormRoute
import com.example.teacherapp.feature.student.navigateToStudentGraph
import com.example.teacherapp.feature.student.studentGraph
import com.example.teacherapp.ui.TeacherAppState
import com.example.teacherapp.ui.nav.graphs.lesson.lessonGraph
import com.example.teacherapp.ui.nav.graphs.lesson.navigateToLessonFormRoute
import com.example.teacherapp.ui.nav.graphs.lesson.navigateToLessonGraph
import com.example.teacherapp.ui.nav.graphs.schedule.scheduleGraph

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

        schoolClassGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar,
            navigateToSchoolYearForm = navController::navigateToSchoolYearFormRoute,
            navigateToStudentGraph = navController::navigateToStudentGraph,
            navigateToStudentFormRoute = navController::navigateToStudentFormRoute,
            navigateToLessonGraph = navController::navigateToLessonGraph,
            navigateToLessonFormRoute = navController::navigateToLessonFormRoute,
        )
        schoolYearGraph(navController = navController, onShowSnackbar = onShowSnackbar)
        studentGraph(navController = navController, onShowSnackbar = onShowSnackbar)
        lessonGraph(navController = navController, onShowSnackbar = onShowSnackbar)

        settingsGraph()
    }
}