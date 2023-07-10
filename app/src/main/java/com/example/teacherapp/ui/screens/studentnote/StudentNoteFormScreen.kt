package com.example.teacherapp.ui.screens.studentnote

import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.StudentNote
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.form.FormStatusContent
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.resource.ResourceContent
import com.example.teacherapp.ui.screens.paramproviders.StudentNotePreviewParameterProvider
import com.example.teacherapp.ui.screens.studentnote.data.StudentNoteFormProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme


@Composable
fun StudentNoteFormScreen(
    studentNoteResource: Resource<StudentNote?>,
    formStatus: FormStatus,
    studentFullName: String,
    title: InputField<String>,
    onTitleChange: (title: String) -> Unit,
    description: InputField<String?>,
    onDescriptionChange: (description: String) -> Unit,
    isValid: Boolean,
    onAddStudentNote: () -> Unit,
    onStudentNoteAdded: () -> Unit,
    isStudentNoteDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(formStatus) {
        if (formStatus == FormStatus.Success) {
            onStudentNoteAdded()
        }
    }

    ResourceContent(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(8.dp),
        resource = studentNoteResource,
        isDeleted = isStudentNoteDeleted,
        deletedMessage = "Usunięto uwagę",
    ) { studentNote ->
        FormStatusContent(
            formStatus = formStatus,
            savingText = "Zapisywanie uwagi...",
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                studentFullName = studentFullName,
                title = title,
                onTitleChange = onTitleChange,
                description = description,
                onDescriptionChange = onDescriptionChange,
                isSubmitEnabled = isValid,
                submitText = if (studentNote == null) "Dodaj uwagę" else "Edytuj uwagę",
                onSubmit = onAddStudentNote,
            )
        }
    }
}

@Composable
private fun Content(
    studentFullName: String,
    title: InputField<String>,
    onTitleChange: (title: String) -> Unit,
    description: InputField<String?>,
    onDescriptionChange: (description: String) -> Unit,
    isSubmitEnabled: Boolean,
    submitText: String,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
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
            capitalization = KeyboardCapitalization.Sentences
        )
        val commonKeyboardActions = KeyboardActions(onNext = { moveNext() })

        Text(studentFullName)

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = title,
            onValueChange = { onTitleChange(it) },
            label = "Tytuł",
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = description,
            onValueChange = { onDescriptionChange(it) },
            label = "Telefon",
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        TeacherOutlinedButton(
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
private fun StudentNoteFormScreenPreview(
    @PreviewParameter(
        StudentNotePreviewParameterProvider::class,
        limit = 1
    ) studentNote: StudentNote,
) {
    TeacherAppTheme {
        Surface {
            val form = StudentNoteFormProvider.createDefaultForm(
                title = studentNote.title,
                description = studentNote.description,
            )

            StudentNoteFormScreen(
                studentNoteResource = Resource.Success(studentNote),
                formStatus = form.status,
                studentFullName = "Jan Kowalski",
                title = form.title,
                onTitleChange = {},
                description = form.description,
                onDescriptionChange = {},
                isValid = form.isValid,
                onAddStudentNote = {},
                onStudentNoteAdded = {},
                isStudentNoteDeleted = false,
            )
        }
    }
}

@Preview
@Composable
private fun StudentNoteFormScreenDeletedPreview() {
    TeacherAppTheme {
        Surface {
            val form = StudentNoteFormProvider.createDefaultForm(
                title = "",
                description = "",
            )

            StudentNoteFormScreen(
                studentNoteResource = Resource.Loading,
                formStatus = form.status,
                studentFullName = "Jan Kowalski",
                title = form.title,
                onTitleChange = {},
                description = form.description,
                onDescriptionChange = {},
                isValid = form.isValid,
                onAddStudentNote = {},
                onStudentNoteAdded = {},
                isStudentNoteDeleted = true,
            )
        }
    }
}