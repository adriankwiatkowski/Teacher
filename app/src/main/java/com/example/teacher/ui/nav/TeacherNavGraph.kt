package com.example.teacher.ui.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.example.teacher.core.ui.util.OnPause
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.auth.nav.AuthNavigation
import com.example.teacher.feature.auth.nav.authGraph
import com.example.teacher.feature.auth.nav.navigateToAuthRoute
import com.example.teacher.feature.grade.nav.gradeGraph
import com.example.teacher.feature.grade.nav.navigateToGradesRoute
import com.example.teacher.feature.lesson.nav.lessonGraph
import com.example.teacher.feature.lesson.nav.navigateToGradeTemplateFormRoute
import com.example.teacher.feature.lesson.nav.navigateToLessonFormRoute
import com.example.teacher.feature.lesson.nav.navigateToLessonGraph
import com.example.teacher.feature.note.nav.noteGraph
import com.example.teacher.feature.schedule.nav.scheduleGraph
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
import com.example.teacher.feature.schoolclass.nav.schoolClassGraph
import com.example.teacher.feature.schoolyear.nav.navigateToSchoolYearFormRoute
import com.example.teacher.feature.schoolyear.nav.schoolYearGraph
import com.example.teacher.feature.settings.nav.settingsGraph
import com.example.teacher.feature.student.nav.navigateToStudentFormRoute
import com.example.teacher.feature.student.nav.navigateToStudentGraph
import com.example.teacher.feature.student.nav.studentGraph
import com.example.teacher.feature.studentnote.nav.navigateToStudentNoteFormRoute
import com.example.teacher.feature.studentnote.nav.studentNoteGraph
import com.example.teacher.ui.TeacherAppState

@Composable
fun TeacherNavGraph(
    appState: TeacherAppState,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    isAuthenticated: Boolean,
    authenticate: () -> Unit,
    unAuthenticate: () -> Unit,
    isDeviceSecure: Boolean,
    enableAuthentication: Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = SchoolClassNavigation.schoolClassGraphRoute,
) {
    val navController = appState.navController

    if (enableAuthentication) {
        AuthenticationHandler(navController = navController, isAuthenticated = isAuthenticated)

        OnPause {
            unAuthenticate()
            navController.navigateToAuthRoute(navOptions { launchSingleTop = true })
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(authenticate = authenticate, isDeviceSecure = isDeviceSecure)

        scheduleGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar
        )

        schoolClassGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            navigateToSchoolYearForm = navController::navigateToSchoolYearFormRoute,
            navigateToStudentGraph = navController::navigateToStudentGraph,
            navigateToStudentFormRoute = navController::navigateToStudentFormRoute,
            navigateToLessonGraph = navController::navigateToLessonGraph,
            navigateToLessonFormRoute = navController::navigateToLessonFormRoute,
        )
        schoolYearGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
        )

        studentGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            navigateToStudentNoteFormRoute = navController::navigateToStudentNoteFormRoute,
        )
        studentNoteGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
        )

        lessonGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            navigateToGradesRoute = navController::navigateToGradesRoute,
        )
        gradeGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            navigateToGradeTemplateFormRoute = navController::navigateToGradeTemplateFormRoute,
        )

        noteGraph(
            navController = navController,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
        )

        settingsGraph(snackbarHostState = snackbarHostState)
    }
}

@Composable
private fun AuthenticationHandler(navController: NavController, isAuthenticated: Boolean) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Handle authentication navigation.
    LaunchedEffect(isAuthenticated, currentRoute) {
        // Navigate to previous (secure) screen if just authenticated.
        if (isAuthenticated && currentRoute == AuthNavigation.authRoute) {
            navController.popBackStack(AuthNavigation.authRoute, inclusive = true)
        }
        // Navigate to authentication screen if not authenticated.
        if (!isAuthenticated) {
            navController.navigateToAuthRoute(navOptions { launchSingleTop = true })
        }
    }
}