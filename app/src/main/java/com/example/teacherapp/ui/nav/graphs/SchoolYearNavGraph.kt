package com.example.teacherapp.ui.nav.graphs

import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.teacherapp.ui.nav.TeacherDestinations
import com.example.teacherapp.ui.screens.schoolyear.SchoolYearFormScreen
import com.example.teacherapp.ui.screens.schoolyear.data.SchoolYearFormViewModel

fun NavGraphBuilder.addSchoolYearGraph(
    navController: NavController,
    setTitle: (String) -> Unit,
) {
    composable(TeacherDestinations.SCHOOL_YEAR_FORM_ROUTE) {
        val viewModel = hiltViewModel<SchoolYearFormViewModel>()
        val form = viewModel.form

        LaunchedEffect(Unit) {
            setTitle("Stwórz nowy rok szkolny")
        }

        SchoolYearFormScreen(
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
}