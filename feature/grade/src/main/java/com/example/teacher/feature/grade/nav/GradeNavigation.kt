package com.example.teacher.feature.grade.nav

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.grade.nav.GradeNavigation.gradeIdArg
import com.example.teacher.feature.grade.nav.GradeNavigation.gradeTemplateIdArg
import com.example.teacher.feature.grade.nav.GradeNavigation.gradesRoute
import com.example.teacher.feature.grade.nav.GradeNavigation.lessonIdArg
import com.example.teacher.feature.grade.nav.GradeNavigation.studentIdArg

private const val gradesScreen = "grades"
private const val gradeFormScreen = "grade-form"

object GradeNavigation {
    internal const val lessonIdArg = "lesson-id"
    internal const val studentIdArg = "student-id"
    internal const val gradeTemplateIdArg = "grade-template-id"
    internal const val gradeIdArg = "grade-id"

    const val gradesRoute = "$gradesScreen/{$lessonIdArg}/{$gradeTemplateIdArg}"
}

private const val gradeFormRoute =
    "$gradeFormScreen/{$gradeTemplateIdArg}/{$studentIdArg}?$gradeIdArg={$gradeIdArg}"

fun NavController.navigateToGradesRoute(
    lessonId: Long,
    gradeTemplateId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$gradesScreen/$lessonId/$gradeTemplateId", navOptions)
}

private fun NavController.navigateToGradeFormRoute(
    gradeTemplateId: Long,
    studentId: Long,
    gradeId: Long?,
    navOptions: NavOptions? = null
) {
    val query = if (gradeId != null) "?$gradeIdArg=$gradeId" else ""
    this.navigate("$gradeFormScreen/$gradeTemplateId/$studentId$query", navOptions)
}

fun NavGraphBuilder.gradeGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    navigateToGradeTemplateFormRoute: (lessonId: Long, gradeTemplateId: Long?) -> Unit,
) {
    composable(
        gradesRoute,
        arguments = listOf(
            navArgument(lessonIdArg) {
                type = NavType.LongType
            },
            navArgument(gradeTemplateIdArg) {
                type = NavType.LongType
            },
        ),
    ) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val lessonId = args.getLong(lessonIdArg)
        val gradeTemplateId = args.getLong(gradeTemplateIdArg)

        GradesRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            onEditClick = {
                navigateToGradeTemplateFormRoute(lessonId, gradeTemplateId)
            },
            onStudentClick = { studentId, gradeId ->
                navController.navigateToGradeFormRoute(
                    gradeTemplateId = gradeTemplateId,
                    studentId = studentId,
                    gradeId = gradeId,
                )
            }
        )
    }

    composable(
        gradeFormRoute,
        arguments = listOf(
            navArgument(gradeTemplateIdArg) {
                type = NavType.LongType
            },
            navArgument(studentIdArg) {
                type = NavType.LongType
            },
            navArgument(gradeIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val isEditMode = args.getLong(gradeIdArg) != 0L

        GradeFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            snackbarHostState = snackbarHostState,
            onShowSnackbar = onShowSnackbar,
            isEditMode = isEditMode,
        )
    }
}