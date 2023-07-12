package com.example.teacherapp.ui.nav.graphs.student

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.nav.graphs.student.route.StudentContentRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentDetailRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentFormRoute
import com.example.teacherapp.ui.nav.graphs.student.route.StudentNoteFormRoute
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab

private const val studentGraphRoute = "student"

internal const val schoolClassIdArg = "school-class-id"
internal const val studentIdArg = "student-id"
internal const val studentNoteIdArg = "student-note-id"

private const val studentDetailScreen = "student"
internal const val studentDetailRoute = "$studentDetailScreen/{$schoolClassIdArg}/{${studentIdArg}}"

private const val studentGradesScreen = "grades"
internal const val studentGradesRoute = "$studentGradesScreen/{$schoolClassIdArg}/{${studentIdArg}}"

private const val studentNotesScreen = "notes"
internal const val studentNotesRoute = "$studentNotesScreen/{$schoolClassIdArg}/{${studentIdArg}}"

private const val studentFormScreen = "student-form"
private const val studentFormRoute =
    "$studentFormScreen/{$schoolClassIdArg}?$studentIdArg={$studentIdArg}"

private const val studentNoteFormScreen = "student-note-form"
private const val studentNoteFormRoute =
    "$studentNoteFormScreen/{$studentIdArg}?$studentNoteIdArg={$studentNoteIdArg}"

fun NavController.navigateToStudentGraph(
    schoolClassId: Long,
    studentId: Long,
    navOptions: NavOptions? = null,
) {
//    this.navigate("$studentDetailScreen/$schoolClassId/$studentId", navOptions)
    this.navigate("$studentGraphRoute/$schoolClassId/$studentId", navOptions)
}

fun NavController.navigateToStudentFormRoute(
    schoolClassId: Long,
    studentId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (studentId != null) "?$studentIdArg=$studentId" else ""
    this.navigate("$studentFormScreen/$schoolClassId$query", navOptions)
}

fun NavGraphBuilder.studentGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    addActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
    removeActionMenuItems: (actionMenuItems: List<ActionMenuItem>) -> Unit,
) {
    navigation(
        startDestination = studentDetailRoute,
        route = studentGraphRoute,
    ) {
        composable(studentGradesRoute) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)
            val studentId = args.getLong(studentIdArg)

            StudentContentRoute(
                selectedTab = StudentTab.Grades,
                onTabClick = { studentTab ->
                    navController.navigateToTab(
                        studentTab = studentTab,
                        schoolClassId = schoolClassId,
                        studentId = studentId,
                    )
                },
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                menuItems = listOf(),
                viewModel = backStackEntry.sharedViewModel(navController = navController),
            ) {
                Text("grades")
            }
        }
        composable(studentNotesRoute) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)
            val studentId = args.getLong(studentIdArg)

            StudentContentRoute(
                selectedTab = StudentTab.Notes,
                onTabClick = { studentTab ->
                    navController.navigateToTab(
                        studentTab = studentTab,
                        schoolClassId = schoolClassId,
                        studentId = studentId,
                    )
                },
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                menuItems = listOf(),
                viewModel = backStackEntry.sharedViewModel(navController = navController),
            ) {
                Text("notes")
            }
        }
        composable(
            studentDetailRoute,
            arguments = listOf(
                navArgument(schoolClassIdArg) {
                    type = NavType.LongType
                },
                navArgument(studentIdArg) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)
            val studentId = args.getLong(studentIdArg)

            val onEditClick = {
                navController.navigateToStudentFormRoute(
                    schoolClassId = schoolClassId,
                    studentId = studentId,
                )
            }

            StudentContentRoute(
                selectedTab = StudentTab.Detail,
                onTabClick = { studentTab ->
                    navController.navigateToTab(
                        studentTab = studentTab,
                        schoolClassId = schoolClassId,
                        studentId = studentId,
                    )
                },
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                menuItems = listOf(
                    ActionMenuItemProvider.edit(onClick = onEditClick),
                ),
                viewModel = backStackEntry.sharedViewModel(navController = navController),
            ) {
                StudentDetailRoute(
                    onEditClick = {
                        navController.navigateToStudentFormRoute(
                            schoolClassId = schoolClassId,
                            studentId = studentId
                        )
                    },
                    onNoteClick = { studentNoteId ->
                        navController.navigateToStudentNoteFormRoute(
                            studentId = studentId,
                            studentNoteId = studentNoteId,
                        )
                    },
                    onAddNoteClick = {
                        navController.navigateToStudentNoteFormRoute(
                            studentId = studentId,
                            studentNoteId = null,
                        )
                    },
                    onNavBack = navController::popBackStack,
                    setTitle = setTitle,
                    showSnackbar = onShowSnackbar,
                    addActionMenuItems = addActionMenuItems,
                    removeActionMenuItems = removeActionMenuItems,
                )
            }
        }

        composable(
            studentNoteFormRoute,
            arguments = listOf(
                navArgument(studentIdArg) {
                    type = NavType.LongType
                },
                navArgument(studentNoteIdArg) {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            ),
        ) {
            StudentNoteFormRoute(
                onNavBack = navController::popBackStack,
                setTitle = setTitle,
                showSnackbar = onShowSnackbar,
                addActionMenuItems = addActionMenuItems,
                removeActionMenuItems = removeActionMenuItems,
            )
        }
    }

    composable(
        studentFormRoute,
        arguments = listOf(
            navArgument(schoolClassIdArg) {
                type = NavType.LongType
            },
            navArgument(studentIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) {
        StudentFormRoute(
            onNavBack = navController::popBackStack,
            setTitle = setTitle,
        )
    }
}

private fun NavController.navigateToStudentNoteFormRoute(
    studentId: Long,
    studentNoteId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (studentNoteId != null) "?$studentNoteIdArg=$studentNoteId" else ""
    this.navigate("$studentNoteFormScreen/$studentId$query", navOptions)
}

private fun NavController.navigateToStudentDetailRoute(
    schoolClassId: Long,
    studentId: Long,
) {
    this.navigateToTab("$studentDetailScreen/$schoolClassId/$studentId")
}

private fun NavController.navigateToStudentGradesRoute(
    schoolClassId: Long,
    studentId: Long,
) {
    this.navigateToTab("$studentGradesScreen/$schoolClassId/$studentId")
}

private fun NavController.navigateToStudentNotesRoute(
    schoolClassId: Long,
    studentId: Long,
) {
    this.navigateToTab("$studentNotesScreen/$schoolClassId/$studentId")
}

private fun NavController.navigateToTab(
    studentTab: StudentTab,
    schoolClassId: Long,
    studentId: Long,
) {
    when (studentTab) {
        StudentTab.Detail -> navigateToStudentDetailRoute(
            schoolClassId = schoolClassId,
            studentId = studentId,
        )
        StudentTab.Grades -> navigateToStudentGradesRoute(
            schoolClassId = schoolClassId,
            studentId = studentId,
        )
        StudentTab.Notes -> navigateToStudentNotesRoute(
            schoolClassId = schoolClassId,
            studentId = studentId,
        )
    }
}

private fun NavController.navigateToTab(route: String) {
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(this@navigateToTab.currentDestination!!.parent!!.id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}