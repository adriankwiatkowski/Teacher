package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.student.StudentFormScreen
import com.example.teacherapp.ui.screens.student.data.StudentFormViewModel

@Composable
internal fun StudentFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentFormViewModel = hiltViewModel(),
) {
    val studentResult by viewModel.studentResult.collectAsStateWithLifecycle()
    val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()
    val form = viewModel.form

    StudentFormScreen(
        modifier = modifier,
        studentResult = studentResult,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        schoolClassName = schoolClassName.orEmpty(),
        formStatus = form.status,
        name = form.name,
        onNameChange = viewModel::onNameChange,
        surname = form.surname,
        onSurnameChange = viewModel::onSurnameChange,
        email = form.email,
        onEmailChange = viewModel::onEmailChange,
        phone = form.phone,
        onPhoneChange = viewModel::onPhoneChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddStudent = viewModel::onSubmit,
        onStudentAdded = onNavBack,
    )
}