package com.example.teacherapp.ui.nav.graphs.lesson

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation.lessonIdArg
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation.schoolClassIdArg
import com.example.teacherapp.ui.nav.graphs.lesson.route.LessonFormRoute

private const val lessonFormScreen = "lesson-form"

object LessonNavigation {
    internal const val schoolClassIdArg = "school-class-id"
    internal const val lessonIdArg = "lesson-id"
}

private const val lessonFormRoute =
    "$lessonFormScreen/{$schoolClassIdArg}?$lessonIdArg={$lessonIdArg}"

fun NavController.navigateToLessonFormRoute(
    schoolClassId: Long,
    lessonId: Long?,
    navOptions: NavOptions? = null,
) {
    val query = if (lessonId != null) "?$lessonIdArg=$lessonId" else ""
    this.navigate("$lessonFormScreen/$schoolClassId$query", navOptions)
}

fun NavGraphBuilder.lessonGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
) {
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
            onNavBack = navController::popBackStack,
            setTitle = setTitle,
        )
    }
}