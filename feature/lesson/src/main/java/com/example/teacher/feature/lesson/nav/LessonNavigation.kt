package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacher.core.ui.component.TeacherDeleteDialog
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.lesson.data.LessonScaffoldViewModel
import com.example.teacher.feature.lesson.nav.LessonNavigation.eventIdArg
import com.example.teacher.feature.lesson.nav.LessonNavigation.gradeTemplateIdArg
import com.example.teacher.feature.lesson.nav.LessonNavigation.lessonIdArg
import com.example.teacher.feature.lesson.nav.LessonNavigation.lessonNoteIdArg
import com.example.teacher.feature.lesson.nav.LessonNavigation.lessonRoute
import com.example.teacher.feature.lesson.nav.LessonNavigation.schoolClassIdArg
import com.example.teacher.feature.lesson.tab.LessonTab

private const val lessonGraphRoute = "lesson"

private const val lessonFormScreen = "lesson-form"
private const val lessonScreen = "lesson"
private const val gradeTemplateFormScreen = "grade-template-form"
private const val lessonNoteFormScreen = "lesson-note-form"
private const val lessonAttendanceScreen = "lesson-attendance"

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

private const val lessonAttendanceRoute = "$lessonAttendanceScreen/{$eventIdArg}"

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
    this.navigate("$lessonAttendanceScreen/$eventId", navOptions)
}

fun NavGraphBuilder.lessonGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    navigateToGradesRoute: (lessonId: Long, gradeTemplateId: Long) -> Unit,
    onAddScheduleClick: (lessonId: Long) -> Unit,
    onEventEditClick: (eventId: Long) -> Unit,
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

            // Handle delete dialog confirmation.
            var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
            if (showDeleteDialog) {
                TeacherDeleteDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    onConfirmClick = {
                        viewModel.onDeleteLesson()
                        showDeleteDialog = false
                    },
                )
            }

            LessonScaffoldWrapper(
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                menuItems = listOf(
                    TeacherActions.edit(onEditClick),
                    TeacherActions.delete({ showDeleteDialog = true }),
                ),
                viewModel = viewModel,
            ) { selectedTab, lesson ->
                when (selectedTab) {
                    LessonTab.Grades -> GradeTemplatesRoute(
                        snackbarHostState = snackbarHostState,
                        lesson = lesson,
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
                        snackbarHostState = snackbarHostState,
                        lesson = lesson,
                        onScheduleAttendanceClick = navController::navigateToLessonAttendanceFormRoute,
                        onAddScheduleClick = { onAddScheduleClick(lessonId) }
                    )

                    LessonTab.Activity -> LessonActivityRoute(
                        snackbarHostState = snackbarHostState,
                        lesson = lesson,
                    )

                    LessonTab.Notes -> NotesRoute(
                        snackbarHostState = snackbarHostState,
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
                onDelete = { navController.popBackStack(route = lessonRoute, inclusive = false) },
                snackbarHostState = snackbarHostState,
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
                snackbarHostState = snackbarHostState,
                onShowSnackbar = onShowSnackbar,
                isEditMode = isEditMode,
            )
        }
    }

    composable(
        lessonAttendanceRoute,
        arguments = listOf(
            navArgument(eventIdArg) {
                type = NavType.LongType
            },
        ),
    ) { backStackEntry ->
        val args = backStackEntry.arguments!!
        val eventId = args.getLong(eventIdArg)

        AttendanceRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            snackbarHostState = snackbarHostState,
            onEventEditClick = { onEventEditClick(eventId) }
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
            snackbarHostState = snackbarHostState,
        )
    }
}