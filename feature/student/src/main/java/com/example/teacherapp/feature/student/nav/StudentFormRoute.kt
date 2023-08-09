package com.example.teacherapp.feature.student.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.student.StudentFormScreen
import com.example.teacherapp.feature.student.data.StudentFormViewModel

@Composable
internal fun StudentFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    viewModel: StudentFormViewModel = hiltViewModel(),
) {
    val studentResult by viewModel.studentResult.collectAsStateWithLifecycle()
    val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()
    val form = viewModel.form
    val formStatus = form.status

    LaunchedEffect(formStatus, onShowSnackbar, onNavBack) {
        if (formStatus == FormStatus.Success) {
            onShowSnackbar("Zapisano dane ucznia")
            onNavBack()
        }
    }

    StudentFormScreen(
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
    )
}