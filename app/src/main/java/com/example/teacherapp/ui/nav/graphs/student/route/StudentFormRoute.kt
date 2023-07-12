package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.student.StudentFormScreen
import com.example.teacherapp.ui.screens.student.data.StudentFormViewModel

@Composable
internal fun StudentFormRoute(
    onNavBack: () -> Unit,
    setTitle: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StudentFormViewModel = hiltViewModel(),
) {
    val studentResource by viewModel.studentResource.collectAsStateWithLifecycle()
    val form = viewModel.form
    val schoolClassName by viewModel.schoolClassName.collectAsStateWithLifecycle()

    LaunchedEffect(schoolClassName) {
        val name = schoolClassName.orEmpty()
        setTitle("Klasa $name")
    }

    StudentFormScreen(
        modifier = modifier,
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
        onAddStudent = viewModel::onSubmit,
        onStudentAdd = onNavBack,
    )
}