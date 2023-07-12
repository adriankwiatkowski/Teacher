package com.example.teacherapp.ui.screens.student

import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.Student
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.form.FormStatusContent
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.resource.ResourceContent
import com.example.teacherapp.ui.screens.paramproviders.StudentPreviewParameterProvider
import com.example.teacherapp.ui.screens.student.data.StudentFormProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun StudentFormScreen(
    studentResource: Resource<Student?>,
    formStatus: FormStatus,
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    surname: InputField<String>,
    onSurnameChange: (surname: String) -> Unit,
    email: InputField<String?>,
    onEmailChange: (email: String) -> Unit,
    phone: InputField<String?>,
    onPhoneChange: (phone: String) -> Unit,
    isValid: Boolean,
    onAddStudent: () -> Unit,
    onStudentAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(formStatus) {
        if (formStatus == FormStatus.Success) {
            onStudentAdd()
        }
    }

    ResourceContent(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(MaterialTheme.spacing.small),
        resource = studentResource,
    ) { student ->
        FormStatusContent(
            formStatus = formStatus,
            savingText = "Zapisywanie studenta...",
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                name = name,
                onNameChange = onNameChange,
                surname = surname,
                onSurnameChange = onSurnameChange,
                email = email,
                onEmailChange = onEmailChange,
                phone = phone,
                onPhoneChange = onPhoneChange,
                isSubmitEnabled = isValid,
                submitText = if (student == null) "Dodaj studenta" else "Edytuj studenta",
                onAddStudent = onAddStudent,
            )
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
    onAddStudent: () -> Unit,
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

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = name,
            onValueChange = { onNameChange(it) },
            label = "Imię",
            leadingIcon = Icons.Default.Person,
            keyboardOptions = nameKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = surname,
            onValueChange = { onSurnameChange(it) },
            label = "Nazwisko",
            leadingIcon = Icons.Default.Person,
            keyboardOptions = nameKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = email,
            onValueChange = { onEmailChange(it) },
            label = "Email",
            leadingIcon = Icons.Default.Email,
            trailingIcon = Icons.Default.CheckCircle,
            onTrailingIconClick = {},
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Email),
            keyboardActions = commonKeyboardActions,
        )

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = phone,
            onValueChange = { onPhoneChange(it) },
            label = "Telefon",
            leadingIcon = Icons.Default.Phone,
            trailingIcon = Icons.Default.CheckCircle,
            onTrailingIconClick = {},
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Phone),
            keyboardActions = commonKeyboardActions,
        )

        TeacherOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddStudent,
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
                studentResource = Resource.Success(student),
                formStatus = form.status,
                name = form.name,
                onNameChange = {},
                surname = form.surname,
                onSurnameChange = {},
                email = form.email,
                onEmailChange = {},
                phone = form.phone,
                onPhoneChange = {},
                isValid = form.isValid,
                onAddStudent = {},
                onStudentAdd = {},
            )
        }
    }
}