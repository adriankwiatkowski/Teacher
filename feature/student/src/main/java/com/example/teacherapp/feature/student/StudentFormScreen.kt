package com.example.teacherapp.feature.student

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.form.FormStatusContent
import com.example.teacherapp.core.ui.component.form.FormTextField
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.student.data.StudentFormProvider
import com.example.teacherapp.feature.student.paramprovider.StudentPreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentFormScreen(
    studentResult: Result<Student?>,
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
    isSubmitEnabled: Boolean,
    onAddStudent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TeacherTopBar(
                title = "Klasa $schoolClassName",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                scrollBehavior = scrollBehavior,
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
                savingText = "Zapisywanie studenta...",
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
                    isSubmitEnabled = isSubmitEnabled,
                    submitText = if (student == null) "Dodaj studenta" else "Edytuj studenta",
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
            label = "Imię",
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            keyboardOptions = nameKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = surname,
            onValueChange = { onSurnameChange(it) },
            label = "Nazwisko",
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            keyboardOptions = nameKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = email,
            onValueChange = { onEmailChange(it) },
            label = "Email",
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                }
            },
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Email),
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = phone,
            onValueChange = { onPhoneChange(it) },
            label = "Telefon",
            leadingIcon = {
                Icon(imageVector = Icons.Default.Phone, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                }
            },
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Phone),
            keyboardActions = commonKeyboardActions,
        )

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        ) {
            Text(text = submitText)
        }
    }
}

@Preview
@Composable
private fun StudentFormScreenPreview(
    @PreviewParameter(StudentPreviewParameterProvider::class, limit = 1) student: Student,
) {
    TeacherAppTheme {
        Surface {
            val form = StudentFormProvider.createDefaultForm(
                id = student.id,
                name = student.name,
                surname = student.surname,
                email = student.email,
                phone = student.phone,
            )

            StudentFormScreen(
                studentResult = Result.Success(student),
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
                isSubmitEnabled = form.isValid,
                onAddStudent = {},
            )
        }
    }
}