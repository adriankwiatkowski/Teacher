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
import com.example.teacherapp.feature.lesson.LessonNavigation.gradeTemplateIdArg
import com.example.teacherapp.feature.lesson.LessonNavigation.lessonIdArg
import com.example.teacherapp.feature.lesson.LessonNavigation.lessonNoteIdArg
import com.example.teacherapp.feature.lesson.LessonNavigation.lessonRoute
import com.example.teacherapp.feature.lesson.LessonNavigation.schoolClassIdArg
import com.example.teacherapp.feature.lesson.data.LessonScaffoldViewModel
import com.example.teacherapp.feature.lesson.route.AttendanceRoute
import com.example.teacherapp.feature.lesson.route.GradeTemplateFormRoute
import com.example.teacherapp.feature.lesson.route.GradeTemplatesRoute
import com.example.teacherapp.feature.lesson.route.LessonActivityRoute
import com.example.teacherapp.feature.lesson.route.LessonFormRoute
import com.example.teacherapp.feature.lesson.route.LessonNoteFormRoute
import com.example.teacherapp.feature.lesson.route.LessonScaffoldWrapper
import com.example.teacherapp.feature.lesson.route.NotesRoute
import com.example.teacherapp.feature.lesson.tab.LessonTab

private const val lessonGraphRoute = "lesson"

private const val lessonFormScreen = "lesson-form"
private const val lessonScreen = "lesson"
private const val gradeTemplateFormScreen = "grade-template-form"
private const val lessonNoteFormScreen = "lesson-note-form"

object LessonNavigation {
    internal const val gradeTemplateIdArg = "grade-template-id"
    internal const val schoolClassIdArg = "school-class-id"
    internal const val lessonIdArg = "lesson-id"
    internal const val lessonNoteIdArg = "lesson-note-id"

    const val lessonRoute = "$lessonScreen/{$schoolClassIdArg}/{$lessonIdArg}"
}

private const val lessonFormRoute =
    "$lessonFormScreen/{$schoolClassIdArg}?$lessonIdArg={$lessonIdArg}"

private const val gradeTemplateFormRoute =
    "$gradeTemplateFormScreen/{$lessonIdArg}?$gradeTemplateIdArg={$gradeTemplateIdArg}"

private const val lessonNoteFormRoute =
    "$lessonNoteFormScreen/{$lessonIdArg}?$lessonNoteIdArg={$lessonNoteIdArg}"

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

fun NavController.navigateToGradeTemplateFormRoute(
    lessonId: Long,
    gradeTemplateId: Long?,
    navOptions: NavOptions? = null
) {
    val query = if (gradeTemplateId != null) "?$gradeTemplateIdArg=$gradeTemplateId" else ""
    this.navigate("$gradeTemplateFormScreen/$lessonId$query", navOptions)
}

private fun NavController.navigateToLessonNoteFormRoute(
    lessonId: Long,
    lessonNoteId: Long?,
    navOptions: NavOptions? = null
) {
    val query = if (lessonNoteId != null) "?$lessonNoteIdArg=$lessonNoteId" else ""
    this.navigate("$lessonNoteFormScreen/$lessonId$query", navOptions)
}

fun NavGraphBuilder.lessonGraph(
    navController: NavController,
    onShowSnackbar: (message: String) -> Unit,
    navigateToGradesRoute: (lessonId: Long, gradeTemplateId: Long) -> Unit,
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
                            navigateToGradesRoute(lessonId, gradeTemplateId)
                        },
                        onAddGradeClick = {
                            navController.navigateToGradeTemplateFormRoute(
                                lessonId = lessonId,
                                gradeTemplateId = null,
                            )
                        },
                    )

                    LessonTab.Attendance -> AttendanceRoute()

                    LessonTab.Activity -> LessonActivityRoute()

                    LessonTab.Notes -> NotesRoute(
                        onNoteClick = { lessonNoteId ->
                            navController.navigateToLessonNoteFormRoute(
                                lessonId = lessonId,
                                lessonNoteId = lessonNoteId,
                            )
                        },
                        onAddNoteClick = {
                            navController.navigateToLessonNoteFormRoute(
                                lessonId = lessonId,
                                lessonNoteId = null,
                            )
                        }
                    )
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
            lessonNoteFormRoute,
            arguments = listOf(
                navArgument(lessonIdArg) {
                    type = NavType.LongType
                },
                navArgument(lessonNoteIdArg) {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val isEditMode = args.getLong(lessonNoteIdArg) != 0L

            LessonNoteFormRoute(
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