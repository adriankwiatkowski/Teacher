package com.example.teacher.feature.studentnote

import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.teacher.core.model.data.StudentNote
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.paramprovider.StudentNotePreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.studentnote.data.StudentNoteFormProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StudentNoteFormScreen(
    studentNoteResult: Result<StudentNote?>,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onDeleteStudentNoteClick: () -> Unit,
    formStatus: FormStatus,
    studentFullName: String,
    title: InputField<String>,
    onTitleChange: (title: String) -> Unit,
    description: InputField<String?>,
    onDescriptionChange: (description: String) -> Unit,
    isNoteNegative: Boolean,
    onIsNoteNegativeChange: (isNegative: Boolean) -> Unit,
    isSubmitEnabled: Boolean,
    onAddStudentNote: () -> Unit,
    isEditMode: Boolean,
    isStudentNoteDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = stringResource(R.string.student_note_form_title),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(TeacherActions.delete(onDeleteStudentNoteClick))
                } else {
                    emptyList()
                },
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
            result = studentNoteResult,
            isDeleted = isStudentNoteDeleted,
            deletedMessage = stringResource(R.string.student_note_note_deleted),
        ) {
            FormStatusContent(
                formStatus = formStatus,
                savingText = stringResource(R.string.student_note_saving_note),
            ) {
                Content(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    studentFullName = studentFullName,
                    title = title,
                    onTitleChange = onTitleChange,
                    description = description,
                    onDescriptionChange = onDescriptionChange,
                    isNoteNegative = isNoteNegative,
                    onIsNoteNegativeChange = onIsNoteNegativeChange,
                    isSubmitEnabled = isSubmitEnabled,
                    submitText = if (isEditMode) {
                        stringResource(R.string.student_note_edit_note)
                    } else {
                        stringResource(R.string.student_note_add_note)
                    },
                    onSubmit = onAddStudentNote,
                )
            }
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
    isNoteNegative: Boolean,
    onIsNoteNegativeChange: (isNegative: Boolean) -> Unit,
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
            capitalization = KeyboardCapitalization.Sentences
        )
        val commonKeyboardActions = KeyboardActions(onNext = { moveNext() })

        Text(text = studentFullName, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.padding(MaterialTheme.spacing.small))

        StudentNoteTypePicker(
            isNoteNegative = isNoteNegative,
            onIsNoteNegativeChange = onIsNoteNegativeChange,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = title,
            onValueChange = { onTitleChange(it) },
            label = stringResource(R.string.student_note_title),
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = description,
            onValueChange = { onDescriptionChange(it) },
            label = stringResource(R.string.student_note_description),
            minLines = 10,
        )

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            label = submitText,
            onClick = onSubmit,
            enabled = isSubmitEnabled,
        )
    }
}

@Composable
private fun StudentNoteTypePicker(
    isNoteNegative: Boolean,
    onIsNoteNegativeChange: (isNegative: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
            Text(
                text = stringResource(R.string.student_note_type),
                style = MaterialTheme.typography.labelLarge,
            )

            Column(Modifier.selectableGroup()) {
                TeacherRadioButton(
                    label = stringResource(R.string.student_note_neutral_type),
                    selected = !isNoteNegative,
                    onClick = { onIsNoteNegativeChange(false) },
                )
                TeacherRadioButton(
                    label = stringResource(R.string.student_note_negative_type),
                    selected = isNoteNegative,
                    onClick = { onIsNoteNegativeChange(true) },
                )
            }
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
    TeacherTheme {
        Surface {
            val form = StudentNoteFormProvider.createDefaultForm(
                title = studentNote.title,
                description = studentNote.description,
            )

            StudentNoteFormScreen(
                studentNoteResult = Result.Success(studentNote),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onDeleteStudentNoteClick = {},
                formStatus = form.status,
                studentFullName = "Jan Kowalski",
                title = form.title,
                onTitleChange = {},
                description = form.description,
                onDescriptionChange = {},
                isNoteNegative = form.isNegative,
                onIsNoteNegativeChange = {},
                isSubmitEnabled = form.isSubmitEnabled,
                onAddStudentNote = {},
                isEditMode = true,
                isStudentNoteDeleted = false,
            )
        }
    }
}

@Preview
@Composable
private fun StudentNoteFormScreenDeletedPreview() {
    TeacherTheme {
        Surface {
            val form = StudentNoteFormProvider.createDefaultForm(
                title = "",
                description = "",
            )

            StudentNoteFormScreen(
                studentNoteResult = Result.Loading,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onDeleteStudentNoteClick = {},
                formStatus = form.status,
                studentFullName = "Jan Kowalski",
                title = form.title,
                onTitleChange = {},
                description = form.description,
                onDescriptionChange = {},
                isNoteNegative = form.isNegative,
                onIsNoteNegativeChange = {},
                isSubmitEnabled = form.isSubmitEnabled,
                onAddStudentNote = {},
                isEditMode = true,
                isStudentNoteDeleted = true,
            )
        }
    }
}