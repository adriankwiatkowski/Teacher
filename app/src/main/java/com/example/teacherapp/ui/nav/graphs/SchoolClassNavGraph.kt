package com.example.teacherapp.ui.nav.graphs

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.nav.graphs.lesson.navigateToLessonFormRoute
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
    setTitle: (String) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
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
        val schoolClassViewModel = hiltViewModel<SchoolClassViewModel>()
        val schoolClassResult by schoolClassViewModel.schoolClassResult.collectAsStateWithLifecycle()
        val isSchoolClassDeleted = schoolClassViewModel.isSchoolClassDeleted

        val args = backStackEntry.arguments!!
        val schoolClassId = args.getLong(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG)

        // Set title.
        LaunchedEffect(schoolClassResult) {
            val schoolClass = schoolClassResult as? Result.Success
            val name = schoolClass?.data?.name.orEmpty()
            val title = "Klasa $name"
            setTitle(title)
        }
        // Observe deletion.
        LaunchedEffect(isSchoolClassDeleted) {
            if (isSchoolClassDeleted) {
                onShowSnackbar("Usunięto klasę")
                navController.navigateUp()
            }
        }
        // Add/remove action menu.
        DisposableEffect(schoolClassViewModel, schoolClassViewModel::deleteSchoolClass) {
            val menuItems = listOf(
                ActionMenuItemProvider.delete(onClick = schoolClassViewModel::deleteSchoolClass),
            )

            addActionMenuItems(menuItems)

            onDispose {
                removeActionMenuItems(menuItems)
            }
        }

        SchoolClassScreen(
            schoolClassResult = schoolClassResult,
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
                navController.navigateToLessonFormRoute(
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
            isSchoolYearExpanded = schoolClassViewModel.isSchoolYearExpanded,
            isStudentsExpanded = schoolClassViewModel.isStudentsExpanded,
            isLessonsExpanded = schoolClassViewModel.isLessonsExpanded,
            isSchoolClassDeleted = isSchoolClassDeleted,
        )
    }

    composable(TeacherDestinations.SCHOOL_CLASS_FORM_ROUTE) {
        val viewModel = hiltViewModel<SchoolClassFormViewModel>()
        val schoolYears by viewModel.schoolYears.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            setTitle("Stwórz nową klasę")
        }

        SchoolClassFormScreen(
            schoolClassName = viewModel.schoolClassName,
            onSchoolClassNameChange = viewModel::onSchoolClassNameChange,
            schoolYears = schoolYears,
            schoolYear = viewModel.schoolYear,
            onSchoolYearChange = viewModel::onSchoolYearChange,
            status = viewModel.status,
            isValid = viewModel.isValid,
            onAddSchoolClass = viewModel::onSubmit,
            onAddSchoolYear = {
                navActions.navigateToSchoolYearFormRoute()
            },
            onSchoolClassAdd = {
                navController.popBackStack()
            }
        )
    }
}