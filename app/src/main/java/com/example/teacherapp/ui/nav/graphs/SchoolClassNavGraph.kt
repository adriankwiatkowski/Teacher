package com.example.teacherapp.ui.nav.graphs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import com.example.teacherapp.ui.nav.TeacherNavigationActions
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassCreatorScreen
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassScreen
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassesScreen
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassCreatorViewModel
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassViewModel
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassesViewModel

fun NavGraphBuilder.addSchoolClassGraph(
    navController: NavController,
    navActions: TeacherNavigationActions,
    setTitle: (String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    addFabAction: (fabAction: FabAction) -> Unit,
    removeFabAction: (fabAction: FabAction) -> Unit,
) {
    composable(TeacherDestinations.SCHOOL_CLASSES_ROUTE) {
        val schoolClassesViewModel = hiltViewModel<SchoolClassesViewModel>()
        val schoolClasses by schoolClassesViewModel.schoolClasses.collectAsStateWithLifecycle()

        SchoolClassesScreen(
            classes = schoolClasses,
            onAddSchoolClassClick = {
                navActions.navigateToSchoolClassCreatorRoute()
            },
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
        val schoolClassResource by schoolClassViewModel.uiState.collectAsStateWithLifecycle()
        val schoolClassId =
            backStackEntry.arguments!!.getLong(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG)
        val isSchoolClassDeleted = schoolClassViewModel.isSchoolClassDeleted

        // Set title.
        LaunchedEffect(schoolClassResource) {
            val schoolClass = schoolClassResource as? Resource.Success
            val title = "Klasa ${schoolClass?.data?.name ?: ""}"
            setTitle(title)
        }
        // Observe deletion.
        LaunchedEffect(isSchoolClassDeleted) {
            if (isSchoolClassDeleted) {
                navController.navigateUp()
            }
        }
        // Add/remove action menu.
        DisposableEffect(schoolClassViewModel, schoolClassViewModel::deleteSchoolClass) {
            val menuItems = listOf(
                ActionMenuItem(
                    name = "",
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    onClick = schoolClassViewModel::deleteSchoolClass,
                ),
            )

            addActionMenuItems(menuItems)

            onDispose {
                removeActionMenuItems(menuItems)
            }
        }

        SchoolClassScreen(
            schoolClassResource = schoolClassResource,
            onStudentClick = { studentId ->
                navActions.navigateToStudentRoute(
                    studentId = studentId,
                )
            },
            onAddStudentClick = {
                navActions.navigateToStudentCreatorRoute(
                    schoolClassId = schoolClassId,
                    studentId = null,
                )
            },
            onLessonClick = { lessonId ->
                navActions.navigateToLessonCreatorRoute(
                    schoolClassId = schoolClassId,
                    lessonId = lessonId,
                )
            },
            onAddLessonClick = {
                navActions.navigateToLessonCreatorRoute(
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

    composable(TeacherDestinations.SCHOOL_CLASS_CREATOR_ROUTE) {
        val viewModel = hiltViewModel<SchoolClassCreatorViewModel>()
        val schoolYears by viewModel.schoolYears.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            setTitle("Stwórz nową klasę")
        }

        SchoolClassCreatorScreen(
            schoolClassName = viewModel.schoolClassName,
            onSchoolClassNameChange = viewModel::onSchoolClassNameChange,
            schoolYears = schoolYears,
            schoolYear = viewModel.schoolYear,
            onSchoolYearChange = viewModel::onSchoolYearChange,
            status = viewModel.status,
            isValid = viewModel.isValid,
            onAddSchoolClass = viewModel::onSubmit,
            onAddSchoolYear = {
                navActions.navigateToSchoolYearCreatorRoute()
            },
            onSchoolClassAdd = {
                navController.popBackStack()
            }
        )
    }
}