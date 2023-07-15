package com.example.teacherapp.ui.nav.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.nav.graphs.lesson.navigateToLessonFormRoute
import com.example.teacherapp.ui.nav.graphs.lesson.navigateToLessonGraph
import com.example.teacherapp.ui.nav.graphs.schoolyear.navigateToSchoolYearFormRoute
import com.example.teacherapp.ui.nav.graphs.student.navigateToStudentFormRoute
import com.example.teacherapp.ui.nav.graphs.student.navigateToStudentGraph
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassFormScreen
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassScreen
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassesScreen
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassFormViewModel
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassViewModel
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassesViewModel

fun NavGraphBuilder.addSchoolClassGraph(
    navController: NavController,
    navActions: TeacherNavigationActions,
    onShowSnackbar: (message: String) -> Unit,
    addFabAction: (fabAction: FabAction) -> Unit,
    removeFabAction: (fabAction: FabAction) -> Unit,
) {
    composable(TeacherDestinations.SCHOOL_CLASSES_ROUTE) {
        val schoolClassesViewModel = hiltViewModel<SchoolClassesViewModel>()
        val schoolClassesResult by schoolClassesViewModel.schoolClassesResult.collectAsStateWithLifecycle()

        SchoolClassesScreen(
            schoolClassesResult = schoolClassesResult,
            onAddSchoolClassClick = navActions::navigateToSchoolClassFormRoute,
            onClassClick = { schoolClassId ->
                navActions.navigateToSchoolClassRoute(schoolClassId = schoolClassId)
            },
            onStudentsClick = { schoolClassId ->
            },
            onLessonsClick = { schoolClassId ->
            },
            addFabAction = addFabAction,
            removeFabAction = removeFabAction,
        )
    }

    composable(
        TeacherDestinations.SCHOOL_CLASS_ROUTE,
        arguments = listOf(
            navArgument(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG) {
                type = NavType.LongType
            },
        ),
    ) { backStackEntry ->
        val viewModel = hiltViewModel<SchoolClassViewModel>()
        val schoolClassResult by viewModel.schoolClassResult.collectAsStateWithLifecycle()
        val isSchoolClassDeleted = viewModel.isSchoolClassDeleted

        val args = backStackEntry.arguments!!
        val schoolClassId = args.getLong(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG)

        // Observe deletion.
        LaunchedEffect(isSchoolClassDeleted) {
            if (isSchoolClassDeleted) {
                onShowSnackbar("Usunięto klasę")
                navController.navigateUp()
            }
        }

        SchoolClassScreen(
            schoolClassResult = schoolClassResult,
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onStudentClick = { studentId ->
                navController.navigateToStudentGraph(
                    schoolClassId = schoolClassId,
                    studentId = studentId,
                )
            },
            onAddStudentClick = {
                navController.navigateToStudentFormRoute(
                    schoolClassId = schoolClassId,
                    studentId = null,
                )
            },
            onLessonClick = { lessonId ->
                navController.navigateToLessonGraph(
                    schoolClassId = schoolClassId,
                    lessonId = lessonId,
                )
            },
            onAddLessonClick = {
                navController.navigateToLessonFormRoute(
                    schoolClassId = schoolClassId,
                    lessonId = null,
                )
            },
            isSchoolYearExpanded = viewModel.isSchoolYearExpanded,
            isStudentsExpanded = viewModel.isStudentsExpanded,
            isLessonsExpanded = viewModel.isLessonsExpanded,
            isSchoolClassDeleted = isSchoolClassDeleted,
            onDeleteSchoolClassClick = viewModel::onDeleteSchoolClass,
        )
    }

    composable(TeacherDestinations.SCHOOL_CLASS_FORM_ROUTE) {
        val viewModel = hiltViewModel<SchoolClassFormViewModel>()
        val schoolYears by viewModel.schoolYears.collectAsStateWithLifecycle()

        SchoolClassFormScreen(
            schoolClassName = viewModel.schoolClassName,
            onSchoolClassNameChange = viewModel::onSchoolClassNameChange,
            schoolYears = schoolYears,
            schoolYear = viewModel.schoolYear,
            onSchoolYearChange = viewModel::onSchoolYearChange,
            status = viewModel.status,
            canSubmit = viewModel.canSubmit,
            onAddSchoolClass = viewModel::onSubmit,
            onAddSchoolYear = navController::navigateToSchoolYearFormRoute,
            onSchoolClassAdded = navController::popBackStack,
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
        )
    }
}