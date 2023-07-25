package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.teacherapp.feature.grade.gradeGraph
import com.example.teacherapp.feature.grade.navigateToGradesRoute
import com.example.teacherapp.feature.lesson.lessonGraph
import com.example.teacherapp.feature.lesson.navigateToGradeTemplateFormRoute
import com.example.teacherapp.feature.lesson.navigateToLessonFormRoute
import com.example.teacherapp.feature.lesson.navigateToLessonGraph
import com.example.teacherapp.feature.note.noteGraph
import com.example.teacherapp.feature.schoolclass.SchoolClassNavigation
import com.example.teacherapp.feature.schoolclass.schoolClassGraph
import com.example.teacherapp.feature.schoolyear.navigateToSchoolYearFormRoute
import com.example.teacherapp.feature.schoolyear.schoolYearGraph
import com.example.teacherapp.feature.settings.settingsGraph
import com.example.teacherapp.feature.student.navigateToStudentFormRoute
import com.example.teacherapp.feature.student.navigateToStudentGraph
import com.example.teacherapp.feature.student.studentGraph
import com.example.teacherapp.feature.studentnote.navigateToStudentNoteFormRoute
import com.example.teacherapp.feature.studentnote.studentNoteGraph
import com.example.teacherapp.ui.TeacherAppState
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

        studentGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar,
            navigateToStudentNoteFormRoute = navController::navigateToStudentNoteFormRoute,
        )
        studentNoteGraph(navController = navController, onShowSnackbar = onShowSnackbar)

        lessonGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar,
            navigateToGradesRoute = navController::navigateToGradesRoute,
        )
        gradeGraph(
            navController = navController,
            onShowSnackbar = onShowSnackbar,
            navigateToGradeTemplateFormRoute = navController::navigateToGradeTemplateFormRoute,
        )

        noteGraph(navController = navController, onShowSnackbar = onShowSnackbar)

        settingsGraph()
    }
}