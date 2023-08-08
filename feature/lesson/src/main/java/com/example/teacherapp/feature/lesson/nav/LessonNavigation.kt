package com.example.teacherapp.feature.lesson.nav

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacherapp.core.ui.provider.ActionItemProvider
import com.example.teacherapp.feature.lesson.data.LessonScaffoldViewModel
import com.example.teacherapp.feature.lesson.nav.LessonNavigation.gradeTemplateIdArg
import com.example.teacherapp.feature.lesson.nav.LessonNavigation.lessonIdArg
import com.example.teacherapp.feature.lesson.nav.LessonNavigation.lessonNoteIdArg
import com.example.teacherapp.feature.lesson.nav.LessonNavigation.lessonRoute
import com.example.teacherapp.feature.lesson.nav.LessonNavigation.eventIdArg
import com.example.teacherapp.feature.lesson.nav.LessonNavigation.schoolClassIdArg
import com.example.teacherapp.feature.lesson.tab.LessonTab

private const val lessonGraphRoute = "lesson"

private const val lessonFormScreen = "lesson-form"
private const val lessonScreen = "lesson"
private const val gradeTemplateFormScreen = "grade-template-form"
private const val lessonNoteFormScreen = "lesson-note-form"
private const val lessonAttendanceFormScreen = "lesson-attendance-form"

object LessonNavigation {
    internal const val gradeTemplateIdArg = "grade-template-id"
    internal const val schoolClassIdArg = "school-class-id"
    internal const val lessonIdArg = "lesson-id"
    internal const val lessonNoteIdArg = "lesson-note-id"
    internal const val eventIdArg = "event-id"

    const val lessonRoute = "$lessonScreen/{$schoolClassIdArg}/{$lessonIdArg}"
}

private const val lessonFormRoute =
    "$lessonFormScreen/{$schoolClassIdArg}?$lessonIdArg={$lessonIdArg}"

private const val gradeTemplateFormRoute =
    "$gradeTemplateFormScreen/{$lessonIdArg}?$gradeTemplateIdArg={$gradeTemplateIdArg}"

private const val lessonNoteFormRoute =
    "$lessonNoteFormScreen/{$lessonIdArg}?$lessonNoteIdArg={$lessonNoteIdArg}"

private const val lessonAttendanceFormRoute = "$lessonAttendanceFormScreen/{$eventIdArg}"

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
    navOptions: NavOptions? = null,
) {
    val query = if (gradeTemplateId != null) "?$gradeTemplateIdArg=$gradeTemplateId" else ""
    this.navigate("$gradeTemplateFormScreen/$lessonId$query", navOptions)
}

private fun NavController.navigateToLessonNoteFormRoute(
    lessonId: Long,
    lessonNoteId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (lessonNoteId != null) "?$lessonNoteIdArg=$lessonNoteId" else ""
    this.navigate("$lessonNoteFormScreen/$lessonId$query", navOptions)
}

private fun NavController.navigateToLessonAttendanceFormRoute(
    eventId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$lessonAttendanceFormScreen/$eventId", navOptions)
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

                    LessonTab.Attendance -> AttendancesRoute(
                        onScheduleAttendanceClick = navController::navigateToLessonAttendanceFormRoute
                    )

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
        lessonAttendanceFormRoute,
        arguments = listOf(
            navArgument(eventIdArg) {
                type = NavType.LongType
            },
        ),
    ) {
        AttendanceFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
        )
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