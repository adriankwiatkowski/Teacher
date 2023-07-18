package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.student.StudentGradesScreen
import com.example.teacherapp.ui.screens.student.data.StudentGradesViewModel

@Composable
internal fun StudentGradesRoute(
    studentGradesViewModel: StudentGradesViewModel = hiltViewModel(),
) {
    val studentGradesResult by studentGradesViewModel.studentGradesResult.collectAsStateWithLifecycle()

    StudentGradesScreen(studentGradesResult = studentGradesResult)
}