package com.example.teacher.feature.lesson

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherFab
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.data.LessonFormProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LessonFormScreen(
    lessonResult: Result<Lesson?>,
    snackbarHostState: SnackbarHostState,
    formStatus: FormStatus,
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    isSubmitEnabled: Boolean,
    schoolClassName: String,
    onAddLessonClick: () -> Unit,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    FormStatusContent(
        modifier = modifier,
        formStatus = formStatus,
        savingText = stringResource(R.string.lesson_saving_lesson),
    ) {
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TeacherTopBar(
                    title = stringResource(R.string.lesson_school_class, schoolClassName),
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = onNavBack,
                    scrollBehavior = scrollBehavior,
                )
            },
            floatingActionButton = {
                TeacherFab(
                    action = TeacherActions.add(onClick = onAddLessonClick),
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
                        submitText = if (lessonResult.data == null) {
                            stringResource(R.string.lesson_add_lesson)
                        } else {
                            stringResource(R.string.lesson_edit_lesson)
                        },
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
                            submitText = if (lesson == null) {
                                stringResource(R.string.lesson_add_lesson)
                            } else {
                                stringResource(R.string.lesson_edit_lesson)
                            },
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
        Text(
            text = stringResource(R.string.lesson_school_class, schoolClassName),
            style = MaterialTheme.typography.headlineMedium,
        )

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

        FormTextField(
            modifier = textFieldModifier,
            inputField = name,
            onValueChange = { onNameChange(it) },
            label = stringResource(R.string.lesson_lesson),
            leadingIcon = {
                val icon = TeacherIcons.subject()
                Icon(imageVector = icon.icon, contentDescription = stringResource(icon.text))
            },
            keyboardOptions = commonKeyboardOptions,
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
private fun LessonFormScreenPreview(
    @PreviewParameter(LessonPreviewParameterProvider::class, limit = 1) lesson: Lesson,
) {
    TeacherTheme {
        Surface {
            val form = LessonFormProvider.createDefaultForm(
                id = lesson.id,
                name = lesson.name,
                status = FormStatus.Idle,
            )

            LessonFormScreen(
                lessonResult = Result.Success(lesson),
                snackbarHostState = remember { SnackbarHostState() },
                formStatus = form.status,
                name = form.name,
                onNameChange = {},
                isSubmitEnabled = form.isSubmitEnabled,
                schoolClassName = lesson.schoolClass.name,
                onAddLessonClick = {},
                showNavigationIcon = true,
                onNavBack = {},
            )
        }
    }
}