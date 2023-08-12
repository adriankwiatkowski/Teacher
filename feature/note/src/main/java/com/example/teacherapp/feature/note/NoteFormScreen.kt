package com.example.teacherapp.feature.note

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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Note
import com.example.teacherapp.core.model.data.NotePriority
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.component.TeacherTopBar
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.form.FormStatusContent
import com.example.teacherapp.core.ui.component.form.FormTextField
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.core.ui.model.InputField
import com.example.teacherapp.core.ui.paramprovider.NotePreviewParameterProvider
import com.example.teacherapp.core.ui.provider.ActionItemProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import com.example.teacherapp.feature.note.component.NotePriorityPicker
import com.example.teacherapp.feature.note.data.NoteFormProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoteFormScreen(
    noteResult: Result<Note?>,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onDeleteNoteClick: () -> Unit,
    formStatus: FormStatus,
    title: InputField<String>,
    onTitleChange: (title: String) -> Unit,
    text: InputField<String>,
    onTextChange: (description: String) -> Unit,
    priority: NotePriority,
    onPriorityChange: (priority: NotePriority) -> Unit,
    isSubmitEnabled: Boolean,
    onAddNote: () -> Unit,
    isEditMode: Boolean,
    isNoteDeleted: Boolean,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = "Notatka",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(ActionItemProvider.delete(onDeleteNoteClick))
                } else {
                    emptyList()
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            result = noteResult,
            isDeleted = isNoteDeleted,
            deletedMessage = "Usunięto notatkę",
        ) {
            FormStatusContent(
                formStatus = formStatus,
                savingText = "Zapisywanie notatki...",
            ) {
                Content(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    title = title,
                    onTitleChange = onTitleChange,
                    text = text,
                    onTextChange = onTextChange,
                    priority = priority,
                    onPriorityChange = onPriorityChange,
                    isSubmitEnabled = isSubmitEnabled,
                    submitText = if (isEditMode) "Edytuj notatkę" else "Dodaj notatkę",
                    onSubmit = onAddNote,
                )
            }
        }
    }
}

@Composable
private fun Content(
    title: InputField<String>,
    onTitleChange: (title: String) -> Unit,
    text: InputField<String>,
    onTextChange: (description: String) -> Unit,
    priority: NotePriority,
    onPriorityChange: (priority: NotePriority) -> Unit,
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

        NotePriorityPicker(priority = priority, onPriorityChange = onPriorityChange)

        FormTextField(
            modifier = textFieldModifier,
            inputField = title,
            onValueChange = onTitleChange,
            label = "Tytuł",
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = Modifier.fillMaxWidth(),
            inputField = text,
            onValueChange = onTextChange,
            label = "Opis",
            minLines = 10,
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
private fun NoteFormScreenPreview(
    @PreviewParameter(
        NotePreviewParameterProvider::class,
        limit = 1
    ) note: Note,
) {
    TeacherAppTheme {
        Surface {
            val form = NoteFormProvider.createDefaultForm(
                title = note.title,
                text = note.text,
                priority = note.priority,
            )

            NoteFormScreen(
                noteResult = Result.Success(note),
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onDeleteNoteClick = {},
                formStatus = form.status,
                title = form.title,
                onTitleChange = {},
                text = form.text,
                onTextChange = {},
                priority = form.priority,
                onPriorityChange = {},
                isSubmitEnabled = form.isValid,
                onAddNote = {},
                isEditMode = true,
                isNoteDeleted = false,
            )
        }
    }
}

@Preview
@Composable
private fun NoteFormScreenDeletedPreview() {
    TeacherAppTheme {
        Surface {
            val form = NoteFormProvider.createDefaultForm()

            NoteFormScreen(
                noteResult = Result.Loading,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                onDeleteNoteClick = {},
                formStatus = form.status,
                title = form.title,
                onTitleChange = {},
                text = form.text,
                onTextChange = {},
                priority = form.priority,
                onPriorityChange = {},
                isSubmitEnabled = form.isValid,
                onAddNote = {},
                isEditMode = true,
                isNoteDeleted = true,
            )
        }
    }
}