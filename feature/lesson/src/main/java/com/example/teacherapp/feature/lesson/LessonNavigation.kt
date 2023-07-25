package com.example.teacherapp.feature.lesson

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacherapp.core.ui.provider.ActionItemProvider
import com.example.teacherapp.feature.lesson.LessonNavigation.gradeIdArg
import com.example.teacherapp.feature.lesson.LessonNavigation.gradeTemplateIdArg
import com.example.teacherapp.feature.lesson.LessonNavigation.gradesRoute
import com.example.teacherapp.feature.lesson.LessonNavigation.lessonIdArg
import com.example.teacherapp.feature.lesson.LessonNavigation.lessonRoute
import com.example.teacherapp.feature.lesson.LessonNavigation.schoolClassIdArg
import com.example.teacherapp.feature.lesson.LessonNavigation.studentIdArg
import com.example.teacherapp.feature.lesson.data.LessonScaffoldViewModel
import com.example.teacherapp.feature.lesson.route.GradeFormRoute
import com.example.teacherapp.feature.lesson.route.GradeTemplateFormRoute
import com.example.teacherapp.feature.lesson.route.GradeTemplatesRoute
import com.example.teacherapp.feature.lesson.route.GradesRoute
import com.example.teacherapp.feature.lesson.route.LessonActivityRoute
import com.example.teacherapp.feature.lesson.route.LessonFormRoute
import com.example.teacherapp.feature.lesson.route.LessonScaffoldWrapper
import com.example.teacherapp.feature.lesson.tab.LessonTab

private const val lessonGraphRoute = "lesson"

private const val lessonFormScreen = "lesson-form"
private const val lessonScreen = "lesson"
private const val gradeTemplateFormScreen = "grade-template-form"
private const val gradesScreen = "grades"
private const val gradeFormScreen = "grade-form"

object LessonNavigation {
    internal const val gradeTemplateIdArg = "grade-template-id"
    internal const val gradeIdArg = "grade-id"
    internal const val schoolClassIdArg = "school-class-id"
    internal const val lessonIdArg = "lesson-id"
    internal const val studentIdArg = "student-id"

    const val lessonRoute = "$lessonScreen/{$schoolClassIdArg}/{$lessonIdArg}"

    const val gradesRoute = "$gradesScreen/{$lessonIdArg}/{$gradeTemplateIdArg}"
}

private const val lessonFormRoute =
    "$lessonFormScreen/{$schoolClassIdArg}?$lessonIdArg={$lessonIdArg}"

private const val gradeTemplateFormRoute =
    "$gradeTemplateFormScreen/{$lessonIdArg}?$gradeTemplateIdArg={$gradeTemplateIdArg}"

private const val gradeFormRoute =
    "$gradeFormScreen/{$gradeTemplateIdArg}/{$studentIdArg}?$gradeIdArg={$gradeIdArg}"

fun NavController.navigateToLessonGraph(
    schoolClassId: Long,
    lessonId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$lessonGraphRoute/$schoolClassId/$lessonId", navOptions)
}

fun NavController.navigateToLessonFormRoute(
    schoolClassId: Long,
    lessonId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (lessonId != null) "?$lessonIdArg=$lessonId" else ""
    this.navigate("$lessonFormScreen/$schoolClassId$query", navOptions)
}

private fun NavController.navigateToGradeTemplateFormRoute(
    lessonId: Long,
    gradeTemplateId: Long?,
    navOptions: NavOptions? = null
) {
    val query = if (gradeTemplateId != null) "?$gradeTemplateIdArg=$gradeTemplateId" else ""
    this.navigate("$gradeTemplateFormScreen/$lessonId$query", navOptions)
}

private fun NavController.navigateToGradesRoute(
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

fun NavGraphBuilder.lessonGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
) {
    navigation(
        startDestination = lessonRoute,
        route = lessonGraphRoute,
    ) {
        composable(
            lessonRoute,
            arguments = listOf(
                navArgument(schoolClassIdArg) {
                    type = NavType.LongType
                },
                navArgument(lessonIdArg) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val viewModel: LessonScaffoldViewModel = hiltViewModel()
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)
            val lessonId = args.getLong(lessonIdArg)

            val onEditClick = {
                navController.navigateToLessonFormRoute(
                    schoolClassId = schoolClassId,
                    lessonId = lessonId,
                )
            }

            LessonScaffoldWrapper(
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                menuItems = listOf(
                    ActionItemProvider.edit(onEditClick),
                    ActionItemProvider.delete(viewModel::onDeleteLesson),
                ),
                viewModel = viewModel,
            ) { selectedTab, _ ->
                when (selectedTab) {
                    LessonTab.Grades -> GradeTemplatesRoute(
                        onGradeClick = { gradeTemplateId ->
                            navController.navigateToGradesRoute(
                                lessonId = lessonId,
                                gradeTemplateId = gradeTemplateId,
                            )
                        },
                        onAddGradeClick = {
                            navController.navigateToGradeTemplateFormRoute(
                                lessonId = lessonId,
                                gradeTemplateId = null,
                            )
                        },
                    )

                    LessonTab.Activity -> LessonActivityRoute()
                }
            }
        }

        composable(
            gradeTemplateFormRoute,
            arguments = listOf(
                navArgument(lessonIdArg) {
                    type = NavType.LongType
                },
                navArgument(gradeTemplateIdArg) {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            ),
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val isEditMode = args.getLong(gradeTemplateIdArg) != 0L

            GradeTemplateFormRoute(
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                isEditMode = isEditMode,
            )
        }

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
                onShowSnackbar = onShowSnackbar,
                onEditClick = {
                    navController.navigateToGradeTemplateFormRoute(
                        lessonId = lessonId,
                        gradeTemplateId = gradeTemplateId,
                    )
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
                onShowSnackbar = onShowSnackbar,
                isEditMode = isEditMode,
            )
        }
    }

    composable(
        lessonFormRoute,
        arguments = listOf(
            navArgument(schoolClassIdArg) {
                type = NavType.LongType
            },
            navArgument(lessonIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) {
        LessonFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
        )
    }
}