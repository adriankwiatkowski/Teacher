package com.example.teacher.feature.student.nav

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.util.BackPressDiscardDialogHandler
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.student.R
import com.example.teacher.feature.student.StudentFormScreen
import com.example.teacher.feature.student.data.StudentFormViewModel

@Composable
internal fun StudentFormRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onShowSnackbar: OnShowSnackbar,
    viewModel: StudentFormViewModel = hiltViewModel(),
) {
    val studentResult by viewModel.studentResult.collectAsStateWithLifecycle()
    val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()
    val form = viewModel.form
    val formStatus = form.status

    // Observe save.
    LaunchedEffect(formStatus) {
        if (formStatus == FormStatus.Success) {
            onShowSnackbar.onShowSnackbar(R.string.student_saved)
            onNavBack()
        }
    }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    BackPressDiscardDialogHandler(
        backPressedDispatcher = backPressedDispatcher,
        onDiscard = onNavBack,
    )

    StudentFormScreen(
        studentResult = studentResult,
        snackbarHostState = snackbarHostState,
        showNavigationIcon = showNavigationIcon,
        onNavBack = { backPressedDispatcher?.onBackPressed() },
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
        registerNumber = form.registerNumber,
        onRegisterNumberChange = viewModel::onRegisterNumberChange,
        isSubmitEnabled = form.isSubmitEnabled,
        onAddStudent = viewModel::onSubmit,
    )
}