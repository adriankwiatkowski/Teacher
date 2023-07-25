package com.example.teacherapp.feature.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.lesson.gradetemplate.GradeTemplatesScreen
import com.example.teacherapp.feature.lesson.gradetemplate.data.GradeTemplatesViewModel

@Composable
internal fun GradeTemplatesRoute(
    onGradeClick: (gradeId: Long) -> Unit,
    onAddGradeClick: () -> Unit,
    viewModel: GradeTemplatesViewModel = hiltViewModel(),
) {
    val gradesResult by viewModel.gradesResult.collectAsStateWithLifecycle()

    GradeTemplatesScreen(
        gradesResult = gradesResult,
        onGradeClick = onGradeClick,
        onAddGradeClick = onAddGradeClick,
    )
}