package com.example.teacherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.teacherapp.ui.screens.lesson.LessonCreatorScreen
import com.example.teacherapp.ui.screens.lesson.data.LessonCreatorViewModel
import com.example.teacherapp.ui.screens.other.ProfileScreen
import com.example.teacherapp.ui.screens.other.SettingsScreen
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassCreatorScreen
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassScreen
import com.example.teacherapp.ui.screens.schoolclass.SchoolClassesScreen
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassCreatorViewModel
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassViewModel
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassesViewModel
import com.example.teacherapp.ui.screens.schoolyear.SchoolYearCreatorScreen
import com.example.teacherapp.ui.screens.schoolyear.data.SchoolYearCreatorViewModel
import com.example.teacherapp.ui.screens.student.StudentCreatorScreen
import com.example.teacherapp.ui.screens.student.data.StudentCreatorViewModel

@Composable
fun TeacherNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = TeacherDestinations.SCHOOL_CLASSES_ROUTE,
    navActions: TeacherNavigationActions = remember(navController) {
        TeacherNavigationActions(navController)
    },
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(TeacherDestinations.CALENDAR_ROUTE) {
            ProfileScreen()
        }

        composable(TeacherDestinations.SETTINGS_ROUTE) {
            SettingsScreen()
        }

        composable(TeacherDestinations.SCHOOL_CLASSES_ROUTE) {
            val schoolClassesViewModel = hiltViewModel<SchoolClassesViewModel>()
            val schoolClasses by schoolClassesViewModel.schoolClasses.collectAsStateWithLifecycle()

            SchoolClassesScreen(
                classes = schoolClasses,
                onAddSchoolClassClick = {
                    navActions.navigateToSchoolClassCreatorRoute()
                },
                onClassClick = { schoolClassId ->
                    navActions.navigateToSchoolClassRoute(schoolClassId = schoolClassId)
                },
                onStudentsClick = { schoolClassId ->
                },
                onLessonsClick = { schoolClassId ->
                }
            )
        }
        composable(
            TeacherDestinations.SCHOOL_CLASS_ROUTE,
            arguments = listOf(
                navArgument(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG) {
                    type = NavType.LongType
                },
            ),
        ) { backStackEntry ->
            val schoolClassViewModel = hiltViewModel<SchoolClassViewModel>()
            val schoolClassResource by schoolClassViewModel.uiState.collectAsStateWithLifecycle()
            val schoolClassId =
                backStackEntry.arguments!!.getLong(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG)

            SchoolClassScreen(
                schoolClassResource = schoolClassResource,
                onStudentClick = { studentId ->
                    navActions.navigateToStudentCreatorRoute(
                        schoolClassId = schoolClassId,
                        studentId = studentId,
                    )
                },
                onAddStudentClick = {
                    navActions.navigateToStudentCreatorRoute(
                        schoolClassId = schoolClassId,
                        studentId = null,
                    )
                },
                onLessonClick = { lessonId ->
                    navActions.navigateToLessonCreatorRoute(
                        schoolClassId = schoolClassId,
                        lessonId = lessonId,
                    )
                },
                onAddLessonClick = {
                    navActions.navigateToLessonCreatorRoute(
                        schoolClassId = schoolClassId,
                        lessonId = null,
                    )
                },
                isSchoolYearExpanded = schoolClassViewModel.isSchoolYearExpanded,
                isStudentsExpanded = schoolClassViewModel.isStudentsExpanded,
                isLessonsExpanded = schoolClassViewModel.isLessonsExpanded,
            )
        }
        composable(TeacherDestinations.SCHOOL_CLASS_CREATOR_ROUTE) {
            val viewModel = hiltViewModel<SchoolClassCreatorViewModel>()
            val schoolYears by viewModel.schoolYears.collectAsStateWithLifecycle()

            SchoolClassCreatorScreen(
                schoolClassName = viewModel.schoolClassName,
                onSchoolClassNameChange = viewModel::onSchoolClassNameChange,
                schoolYears = schoolYears,
                schoolYear = viewModel.schoolYear,
                onSchoolYearChange = viewModel::onSchoolYearChange,
                status = viewModel.status,
                isValid = viewModel.isValid,
                onAddSchoolClass = viewModel::onSubmit,
                onAddSchoolYear = {
                    navActions.navigateToSchoolYearCreatorRoute()
                },
                onSchoolClassAdd = {
                    navController.popBackStack()
                }
            )
        }

        composable(TeacherDestinations.SCHOOL_YEAR_CREATOR_ROUTE) {
            val viewModel = hiltViewModel<SchoolYearCreatorViewModel>()
            val form = viewModel.form

            SchoolYearCreatorScreen(
                termForms = form.termForms,
                schoolYearName = form.schoolYearName,
                onSchoolYearNameChange = viewModel::onSchoolYearNameChange,
                onTermNameChange = viewModel::onTermNameChange,
                onStartDateChange = viewModel::onStartDateChange,
                onEndDateChange = viewModel::onEndDateChange,
                status = form.status,
                isValid = form.isValid,
                onAddSchoolYear = viewModel::onAddSchoolYear,
                onSchoolYearAdd = { navController.popBackStack() }
            )
        }

        composable(
            TeacherDestinations.STUDENT_CREATOR_ROUTE,
            arguments = listOf(
                navArgument(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG) {
                    type = NavType.LongType
                },
                navArgument(TeacherDestinationsArgs.STUDENT_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            ),
        ) {
            val viewModel: StudentCreatorViewModel = hiltViewModel()
            val studentResource by viewModel.studentResource.collectAsStateWithLifecycle()
            val form = viewModel.form
            val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()

            StudentCreatorScreen(
                studentResource = studentResource,
                formStatus = form.status,
                name = form.name,
                onNameChange = viewModel::onNameChange,
                surname = form.surname,
                onSurnameChange = viewModel::onSurnameChange,
                email = form.email,
                onEmailChange = viewModel::onEmailChange,
                phone = form.phone,
                onPhoneChange = viewModel::onPhoneChange,
                isValid = form.isValid,
                schoolClassName = schoolClassName.orEmpty(),
                onAddStudent = viewModel::onSubmit,
                onStudentAdd = { navController.popBackStack() },
            )
        }

        composable(
            TeacherDestinations.LESSON_CREATOR_ROUTE,
            arguments = listOf(
                navArgument(TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG) {
                    type = NavType.LongType
                },
                navArgument(TeacherDestinationsArgs.LESSON_ID_ARG) {
                    type = NavType.LongType
                    defaultValue = 0L
                },
            ),
        ) {
            val viewModel: LessonCreatorViewModel = hiltViewModel()
            val lessonResource by viewModel.lessonResource.collectAsStateWithLifecycle()
            val form = viewModel.form
            val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()

            LessonCreatorScreen(
                lessonResource = lessonResource,
                formStatus = form.status,
                name = form.name,
                onNameChange = viewModel::onNameChange,
                isValid = form.isValid,
                schoolClassName = schoolClassName.orEmpty(),
                onAddLesson = viewModel::onSubmit,
                onLessonAdd = navController::popBackStack,
            )
        }
    }
}