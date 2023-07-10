package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.ui.nav.graphs.*

@Composable
fun TeacherNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = TeacherDestinations.SCHOOL_CLASSES_ROUTE,
    setTitle: (String) -> Unit,
    showSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    addFabAction: (fabAction: FabAction) -> Unit,
    removeFabAction: (fabAction: FabAction) -> Unit,
    navActions: TeacherNavigationActions = remember(navController) {
        TeacherNavigationActions(navController)
    },
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        addScheduleGraph()

        addSchoolClassGraph(
            navController = navController,
            navActions = navActions,
            setTitle = setTitle,
            showSnackbar = showSnackbar,
            addActionMenuItems = addActionMenuItems,
            removeActionMenuItems = removeActionMenuItems,
            addFabAction = addFabAction,
            removeFabAction = removeFabAction,
        )
        addSchoolYearGraph(
            navController = navController,
            setTitle = setTitle,
        )

        addStudentRouteGraph(
            navController = navController,
            navActions = navActions,
            setTitle = setTitle,
            showSnackbar = showSnackbar,
            addActionMenuItems = addActionMenuItems,
            removeActionMenuItems = removeActionMenuItems,
        )
        addStudentNoteRouteGraph(
            navController = navController,
            navActions = navActions,
            setTitle = setTitle,
            showSnackbar = showSnackbar,
            addActionMenuItems = addActionMenuItems,
            removeActionMenuItems = removeActionMenuItems,
        )

        addLessonGraph(
            navController = navController,
            setTitle = setTitle,
        )

        addSettingsGraph()
    }
}