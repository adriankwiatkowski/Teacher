package com.example.teacherapp.ui.nav.graphs.lesson

import androidx.compose.material.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation.gradeTemplateIdArg
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation.lessonIdArg
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation.lessonRoute
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation.schoolClassIdArg
import com.example.teacherapp.ui.nav.graphs.lesson.route.GradeTemplateFormRoute
import com.example.teacherapp.ui.nav.graphs.lesson.route.GradeTemplatesRoute
import com.example.teacherapp.ui.nav.graphs.lesson.route.LessonFormRoute
import com.example.teacherapp.ui.nav.graphs.lesson.route.LessonScaffoldWrapper
import com.example.teacherapp.ui.nav.graphs.lesson.tab.LessonTab
import com.example.teacherapp.ui.screens.lesson.data.LessonScaffoldViewModel

private const val lessonGraphRoute = "lesson"

private const val lessonFormScreen = "lesson-form"
private const val lessonScreen = "lesson"
private const val gradeTemplateFormScreen = "grade-template-form"

internal object LessonNavigation {
    internal const val gradeTemplateIdArg = "grade-template-id"
    internal const val schoolClassIdArg = "school-class-id"
    internal const val lessonIdArg = "lesson-id"

    internal const val lessonRoute = "$lessonScreen/{$schoolClassIdArg}/{$lessonIdArg}"
}

private const val lessonFormRoute =
    "$lessonFormScreen/{$schoolClassIdArg}?$lessonIdArg={$lessonIdArg}"

private const val gradeTemplateFormRoute =
    "$gradeTemplateFormScreen/{$lessonIdArg}?$gradeTemplateIdArg={$gradeTemplateIdArg}"

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
                    ActionMenuItemProvider.edit(onEditClick),
                    ActionMenuItemProvider.delete(viewModel::onDeleteLesson),
                ),
                viewModel = viewModel,
            ) { selectedTab, lesson ->
                when (selectedTab) {
                    LessonTab.Grades -> GradeTemplatesRoute(
                        onGradeClick = { gradeTemplateId ->
                            navController.navigateToGradeTemplateFormRoute(
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

                    LessonTab.Activity -> {
                        Text(text = "Aktywność")
                    }
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
        LessonFormRoute(onNavBack = navController::popBackStack)
    }
}