package com.example.teacherapp.ui.screens.lesson

import android.view.KeyEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.TeacherFab
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.form.FormStatusContent
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.resource.ResultContent
import com.example.teacherapp.ui.screens.lesson.data.LessonFormProvider
import com.example.teacherapp.ui.screens.paramproviders.LessonPreviewParameterProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun LessonFormScreen(
    lessonResult: Result<Lesson?>,
    formStatus: FormStatus,
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    isSubmitEnabled: Boolean,
    schoolClassName: String,
    onAddLessonClick: () -> Unit,
    onLessonAdded: () -> Unit,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(formStatus) {
        if (formStatus == FormStatus.Success) {
            onLessonAdded()
        }
    }

    FormStatusContent(
        modifier = modifier,
        formStatus = formStatus,
        savingText = "Zapisywanie przedmiotu...",
    ) {
        Scaffold(
            topBar = {
                TeacherTopBar(
                    title = "Klasa $schoolClassName",
                    showNavigationIcon = true,
                    onNavigationIconClick = onNavBack,
                )
            },
            floatingActionButton = {
                TeacherFab(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    onClick = onAddLessonClick,
                    visible = isSubmitEnabled,
                )
            },
            floatingActionButtonPosition = FabPosition.End,
        ) { innerPadding ->
            // TODO: For some reason, sometimes ResourceContent doesn't get updated on Resource.Success.
            Box(Modifier.padding(innerPadding)) {
                if (lessonResult is Result.Success) {
                    Content(
                        modifier = Modifier
                            .padding(MaterialTheme.spacing.small)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        schoolClassName = schoolClassName,
                        name = name,
                        onNameChange = onNameChange,
                        onSubmit = onAddLessonClick,
                        isSubmitEnabled = isSubmitEnabled,
                        submitText = if (lessonResult.data == null) "Dodaj przedmiot" else "Edytuj przedmiot",
                    )
                } else {
                    ResultContent(result = lessonResult) { lesson ->
                        Content(
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.small)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            schoolClassName = schoolClassName,
                            name = name,
                            onNameChange = onNameChange,
                            submitText = if (lesson == null) "Dodaj przedmiot" else "Edytuj przedmiot",
                            isSubmitEnabled = isSubmitEnabled,
                            onSubmit = onAddLessonClick,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Content(
    schoolClassName: String,
    name: InputField<String>,
    onNameChange: (String) -> Unit,
    submitText: String,
    isSubmitEnabled: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = "Klasa $schoolClassName", style = MaterialTheme.typography.headlineMedium)

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
        val commonKeyboardActions = KeyboardActions(onNext = { moveNext() })

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = name,
            onValueChange = { onNameChange(it) },
            label = "Przedmiot",
            leadingIcon = Icons.Default.Subject,
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
private fun LessonFormScreenPreview(
    @PreviewParameter(LessonPreviewParameterProvider::class, limit = 2) lesson: Lesson,
) {
    TeacherAppTheme {
        Surface {
            val form = LessonFormProvider.createDefaultForm(
                id = lesson.id,
                name = lesson.name,
                status = FormStatus.Idle,
            )

            LessonFormScreen(
                lessonResult = Result.Success(lesson),
                formStatus = form.status,
                name = form.name,
                onNameChange = {},
                isSubmitEnabled = form.isValid,
                schoolClassName = lesson.schoolClass.name,
                onAddLessonClick = {},
                onLessonAdded = {},
                onNavBack = {},
            )
        }
    }
}