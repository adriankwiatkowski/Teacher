package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.teacherapp.feature.grade.nav.gradeGraph
import com.example.teacherapp.feature.grade.nav.navigateToGradesRoute
import com.example.teacherapp.feature.lesson.nav.lessonGraph
import com.example.teacherapp.feature.lesson.nav.navigateToGradeTemplateFormRoute
import com.example.teacherapp.feature.lesson.nav.navigateToLessonFormRoute
import com.example.teacherapp.feature.lesson.nav.navigateToLessonGraph
import com.example.teacherapp.feature.note.nav.noteGraph
import com.example.teacherapp.feature.schedule.nav.scheduleGraph
import com.example.teacherapp.feature.schoolclass.nav.SchoolClassNavigation
import com.example.teacherapp.feature.schoolclass.nav.schoolClassGraph
import com.example.teacherapp.feature.schoolyear.nav.navigateToSchoolYearFormRoute
import com.example.teacherapp.feature.schoolyear.nav.schoolYearGraph
import com.example.teacherapp.feature.settings.nav.settingsGraph
import com.example.teacherapp.feature.student.nav.navigateToStudentFormRoute
import com.example.teacherapp.feature.student.nav.navigateToStudentGraph
import com.example.teacherapp.feature.student.nav.studentGraph
import com.example.teacherapp.feature.studentnote.nav.navigateToStudentNoteFormRoute
import com.example.teacherapp.feature.studentnote.nav.studentNoteGraph
import com.example.teacherapp.ui.TeacherAppState

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
        scheduleGraph(navController = navController, onShowSnackbar = onShowSnackbar)

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