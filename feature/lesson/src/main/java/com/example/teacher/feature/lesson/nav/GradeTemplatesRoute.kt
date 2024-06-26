package com.example.teacher.feature.lesson.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.feature.lesson.gradetemplate.GradeTemplatesScreen
import com.example.teacher.feature.lesson.gradetemplate.data.GradeTemplatesViewModel

@Composable
internal fun GradeTemplatesRoute(
    snackbarHostState: SnackbarHostState,
    lesson: Lesson,
    onGradeClick: (gradeId: Long) -> Unit,
    onAddGradeClick: () -> Unit,
    viewModel: GradeTemplatesViewModel = hiltViewModel(),
) {
    val gradeTemplateUiStateResult by viewModel.gradeTemplateUiStateResult.collectAsStateWithLifecycle()

    GradeTemplatesScreen(
        gradeTemplateUiStateResult = gradeTemplateUiStateResult,
        snackbarHostState = snackbarHostState,
        lesson = lesson,
        onGradeClick = onGradeClick,
        onAddGradeClick = onAddGradeClick,
    )
}