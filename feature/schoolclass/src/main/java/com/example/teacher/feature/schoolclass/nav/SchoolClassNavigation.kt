package com.example.teacher.feature.schoolclass.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.ui.component.TeacherDeleteDialog
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.schoolclass.data.SchoolClassFormViewModel
import com.example.teacher.feature.schoolclass.data.SchoolClassViewModel
import com.example.teacher.feature.schoolclass.data.SchoolClassesViewModel
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation.deletedSchoolYearIdArg
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation.schoolClassGraphRoute
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation.schoolClassIdArg
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation.schoolClassRoute
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation.schoolClassesRoute
import com.example.teacher.feature.schoolclass.tab.SchoolClassTab

private const val schoolClassesScreen = "school-classes"
private const val schoolClassScreen = "school-class"
private const val schoolClassFormScreen = "school-class-form"

object SchoolClassNavigation {
    const val schoolClassGraphRoute = "school-class"

    internal const val schoolClassIdArg = "school-class-id"
    internal const val deletedSchoolYearIdArg = "deleted-school-year-id"

    const val schoolClassesRoute = schoolClassesScreen
    const val schoolClassRoute = "$schoolClassScreen/{$schoolClassIdArg}"

    fun onDeleteSchoolYear(navController: NavController, schoolYearId: Long) {
        val prevBackStack = navController.previousBackStackEntry
        prevBackStack?.savedStateHandle?.set(deletedSchoolYearIdArg, schoolYearId)
    }
}

private const val schoolClassFormRoute =
    "$schoolClassFormScreen?$schoolClassIdArg={$schoolClassIdArg}"

fun NavController.navigateToSchoolClassGraph(navOptions: NavOptions? = null) {
    this.navigate(schoolClassGraphRoute, navOptions)
}

private fun NavController.navigateToSchoolClassRoute(
    schoolClassId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate("$schoolClassScreen/$schoolClassId", navOptions)
}

private fun NavController.navigateToSchoolClassFormRoute(
    schoolClassId: Long? = null,
    navOptions: NavOptions? = null,
) {
    val query = if (schoolClassId != null) "?$schoolClassIdArg=$schoolClassId" else ""
    this.navigate("$schoolClassFormScreen$query", navOptions)
}

fun NavGraphBuilder.schoolClassGraph(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    onSchoolClassDeleted: () -> Unit,
    navigateToSchoolYearForm: () -> Unit,
    navigateToSchoolYearEditForm: (schoolYearId: Long) -> Unit,
    navigateToStudentGraph: (schoolClassId: Long, studentId: Long) -> Unit,
    navigateToStudentFormRoute: (schoolClassId: Long, studentId: Long?) -> Unit,
    navigateToLessonGraph: (schoolClassId: Long, lessonId: Long) -> Unit,
    navigateToLessonFormRoute: (schoolClassId: Long, lessonId: Long?) -> Unit,
) {
    navigation(
        startDestination = schoolClassesRoute,
        route = schoolClassGraphRoute
    ) {
        composable(schoolClassesRoute) {
            val viewModel = hiltViewModel<SchoolClassesViewModel>()

            SchoolClassesRoute(
                snackbarHostState = snackbarHostState,
                onAddSchoolClassClick = navController::navigateToSchoolClassFormRoute,
                onClassClick = { schoolClassId ->
                    navController.navigateToSchoolClassRoute(schoolClassId = schoolClassId)
                },
                viewModel = viewModel,
            )
        }

        composable(
            schoolClassRoute,
            arguments = listOf(
                navArgument(schoolClassIdArg) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val viewModel: SchoolClassViewModel = hiltViewModel()
            val args = backStackEntry.arguments!!
            val schoolClassId = args.getLong(schoolClassIdArg)

            val onEditClick = {
                navController.navigateToSchoolClassFormRoute(schoolClassId = schoolClassId)
            }

            // Handle delete dialog confirmation.
            var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
            if (showDeleteDialog) {
                TeacherDeleteDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    onConfirmClick = {
                        viewModel.onDelete()
                        showDeleteDialog = false
                    },
                )
            }

            SchoolClassScaffoldWrapper(
                showNavigationIcon = true,
                onNavBack = navController::popBackStack,
                onShowSnackbar = onShowSnackbar,
                menuItems = listOf(
                    TeacherActions.edit(onClick = onEditClick),
                    TeacherActions.delete(onClick = { showDeleteDialog = true }),
                ),
                viewModel = viewModel,
            ) { selectedTab, schoolClass ->
                when (selectedTab) {
                    SchoolClassTab.Detail -> SchoolClassDetailRoute(
                        snackbarHostState = snackbarHostState,
                        schoolYear = schoolClass.schoolYear,
                    )

                    SchoolClassTab.Students -> SchoolClassStudentsRoute(
                        snackbarHostState = snackbarHostState,
                        students = schoolClass.students,
                        onStudentClick = { studentId ->
                            navigateToStudentGraph(schoolClassId, studentId)
                        },
                        onAddStudentClick = {
                            navigateToStudentFormRoute(schoolClassId, null)
                        },
                        service = viewModel,
                    )

                    SchoolClassTab.Subjects -> SchoolClassLessonsRoute(
                        snackbarHostState = snackbarHostState,
                        lessons = schoolClass.lessons,
                        onLessonClick = { lessonId ->
                            navigateToLessonGraph(schoolClassId, lessonId)
                        },
                        onAddLessonClick = {
                            navigateToLessonFormRoute(schoolClassId, null)
                        },
                    )
                }
            }
        }
    }

    composable(
        schoolClassFormRoute,
        arguments = listOf(
            navArgument(schoolClassIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
    ) { backStackEntry ->
        val viewModel: SchoolClassFormViewModel = hiltViewModel()

        val args = backStackEntry.arguments!!
        val isEditMode = args.getLong(schoolClassIdArg) != 0L

        val schoolYearId = getSchoolYearId(viewModel, backStackEntry.savedStateHandle)

        val deletedSchoolYearId by backStackEntry
            .savedStateHandle
            .getStateFlow(deletedSchoolYearIdArg, 0L)
            .collectAsState()

        // Observe school year deletion.
        LaunchedEffect(isEditMode, schoolYearId, deletedSchoolYearId) {
            // Only navigate to previous screen if
            // current school class was in deleted school year.
            if (isEditMode && schoolYearId == deletedSchoolYearId) {
                onSchoolClassDeleted()
            }
        }

        SchoolClassFormRoute(
            showNavigationIcon = true,
            onNavBack = navController::popBackStack,
            snackbarHostState = snackbarHostState,
            isEditMode = isEditMode,
            onAddSchoolYear = navigateToSchoolYearForm,
            onEditSchoolYear = navigateToSchoolYearEditForm,
            viewModel = viewModel,
        )
    }
}

@Composable
private fun getSchoolYearId(
    viewModel: SchoolClassFormViewModel,
    savedStateHandle: SavedStateHandle,
): Long? {
    // Save local copy of id, so we will know if  school year form deleted our year.
    val localSchoolYearIdKey = "local-school-year-id"

    val schoolClassResult by viewModel.schoolClassResult.collectAsState()
    val schoolClass =
        remember(schoolClassResult) { (schoolClassResult as? Result.Success)?.data }
    val schoolYearId = schoolClass?.schoolYear?.id

    if (schoolYearId != null) {
        savedStateHandle[localSchoolYearIdKey] = schoolYearId
    }

    return savedStateHandle[localSchoolYearIdKey]
}