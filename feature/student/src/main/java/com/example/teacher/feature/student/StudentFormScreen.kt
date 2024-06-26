package com.example.teacher.feature.student

import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.paramprovider.StudentPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.student.data.StudentFormProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentFormScreen(
    studentResult: Result<Student?>,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    schoolClassName: String,
    formStatus: FormStatus,
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    surname: InputField<String>,
    onSurnameChange: (surname: String) -> Unit,
    email: InputField<String?>,
    onEmailChange: (email: String) -> Unit,
    phone: InputField<String?>,
    onPhoneChange: (phone: String) -> Unit,
    registerNumber: InputField<String?>,
    onRegisterNumberChange: (registerNumber: String) -> Unit,
    isSubmitEnabled: Boolean,
    onAddStudent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = stringResource(R.string.student_school_class_name, schoolClassName),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
                closeIcon = true,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            result = studentResult,
        ) { student ->
            FormStatusContent(
                formStatus = formStatus,
                savingText = stringResource(R.string.student_saving_student),
            ) {
                Content(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    name = name,
                    onNameChange = onNameChange,
                    surname = surname,
                    onSurnameChange = onSurnameChange,
                    email = email,
                    onEmailChange = onEmailChange,
                    phone = phone,
                    onPhoneChange = onPhoneChange,
                    registerNumber = registerNumber,
                    onRegisterNumberChange = onRegisterNumberChange,
                    isSubmitEnabled = isSubmitEnabled,
                    submitText = if (student == null) {
                        stringResource(R.string.add_student)
                    } else {
                        stringResource(R.string.edit_student)
                    },
                    onSubmit = onAddStudent,
                )
            }
        }
    }
}

@Composable
private fun Content(
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    surname: InputField<String>,
    onSurnameChange: (surname: String) -> Unit,
    email: InputField<String?>,
    onEmailChange: (email: String) -> Unit,
    phone: InputField<String?>,
    onPhoneChange: (phone: String) -> Unit,
    registerNumber: InputField<String?>,
    onRegisterNumberChange: (registerNumber: String) -> Unit,
    isSubmitEnabled: Boolean,
    submitText: String,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        val focusManager = LocalFocusManager.current
        val movePrev = { focusManager.moveFocus(FocusDirection.Up) }
        val moveNext = { focusManager.moveFocus(FocusDirection.Down) }

        val textFieldModifier = Modifier
            .fillMaxWidth()
            .onKeyEvent { keyEvent ->
                when (keyEvent.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_ENTER,
                    KeyEvent.KEYCODE_TAB -> {
                        if (keyEvent.isShiftPressed) movePrev() else moveNext()
                        true
                    }

                    else -> false
                }
            }
        val commonKeyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
        )
        val nameKeyboardOptions = commonKeyboardOptions.copy(
            capitalization = KeyboardCapitalization.Words,
        )
        val commonKeyboardActions = KeyboardActions(onNext = { moveNext() })

        FormTextField(
            modifier = textFieldModifier,
            inputField = name,
            onValueChange = { onNameChange(it) },
            label = stringResource(R.string.student_name),
            leadingIcon = {
                val icon = TeacherIcons.person()
                Icon(imageVector = icon.icon, contentDescription = null)
            },
            keyboardOptions = nameKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = surname,
            onValueChange = { onSurnameChange(it) },
            label = stringResource(R.string.student_surname),
            leadingIcon = {
                val icon = TeacherIcons.person()
                Icon(imageVector = icon.icon, contentDescription = null)
            },
            keyboardOptions = nameKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = email,
            onValueChange = { onEmailChange(it) },
            label = stringResource(R.string.student_email),
            leadingIcon = {
                val icon = TeacherIcons.email()
                Icon(imageVector = icon.icon, contentDescription = null)
            },
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Email),
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = phone,
            onValueChange = { onPhoneChange(it) },
            label = stringResource(R.string.student_phone),
            leadingIcon = {
                val icon = TeacherIcons.phone()
                Icon(imageVector = icon.icon, contentDescription = null)
            },
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Phone),
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = registerNumber,
            onValueChange = { onRegisterNumberChange(it) },
            label = stringResource(R.string.student_register_number),
            leadingIcon = {
                val icon = TeacherIcons.registerBook()
                Icon(imageVector = icon.icon, contentDescription = null)
            },
            keyboardOptions = nameKeyboardOptions.copy(keyboardType = KeyboardType.Number),
            keyboardActions = commonKeyboardActions,
        )

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            label = submitText,
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        )
    }
}

@Preview
@Composable
private fun StudentFormScreenPreview(
    @PreviewParameter(StudentPreviewParameterProvider::class, limit = 1) student: Student,
) {
    TeacherTheme {
        Surface {
            val form = StudentFormProvider.createDefaultForm(
                id = student.id,
                name = student.name,
                surname = student.surname,
                email = student.email,
                phone = student.phone,
                usedRegisterNumbers = emptyList(),
            )

            StudentFormScreen(
                studentResult = Result.Success(student),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                schoolClassName = "1A",
                formStatus = form.status,
                name = form.name,
                onNameChange = {},
                surname = form.surname,
                onSurnameChange = {},
                email = form.email,
                onEmailChange = {},
                phone = form.phone,
                onPhoneChange = {},
                registerNumber = form.registerNumber,
                onRegisterNumberChange = {},
                isSubmitEnabled = form.isSubmitEnabled,
                onAddStudent = {},
            )
        }
    }
}