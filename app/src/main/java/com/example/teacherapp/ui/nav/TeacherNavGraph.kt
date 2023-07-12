package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.FabAction
import com.example.teacherapp.ui.TeacherAppState
import com.example.teacherapp.ui.nav.graphs.*
import com.example.teacherapp.ui.nav.graphs.student.studentGraph

@Composable
fun TeacherNavGraph(
    appState: TeacherAppState,
    setTitle: (String) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    addFabAction: (fabAction: FabAction) -> Unit,
    removeFabAction: (fabAction: FabAction) -> Unit,
    modifier: Modifier = Modifier,
    startDestination: String = TeacherDestinations.SCHOOL_CLASSES_ROUTE,
) {
    val navController = appState.navController
    val navActions = appState.navActions

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
            showSnackbar = onShowSnackbar,
            addActionMenuItems = addActionMenuItems,
            removeActionMenuItems = removeActionMenuItems,
            addFabAction = addFabAction,
            removeFabAction = removeFabAction,
        )
        addSchoolYearGraph(
            navController = navController,
            setTitle = setTitle,
        )

        studentGraph(
            navController = navController,
            setTitle = setTitle,
            onShowSnackbar = onShowSnackbar,
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