package com.example.teacher.feature.lesson.gradetemplate

import android.view.KeyEvent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.teacher.core.model.data.GradeTemplate
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.component.TeacherTopBar
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.form.FormStatusContent
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.paramprovider.LessonPreviewParameterProvider
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.R
import com.example.teacher.feature.lesson.gradetemplate.data.GradeTemplateFormProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GradeTemplateFormScreen(
    gradeTemplateResult: Result<GradeTemplate?>,
    lesson: Lesson?,
    snackbarHostState: SnackbarHostState,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    formStatus: FormStatus,
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    description: InputField<String?>,
    onDescriptionChange: (description: String) -> Unit,
    weight: InputField<String>,
    onWeightChange: (weight: String) -> Unit,
    isFirstTerm: Boolean,
    onIsFirstTermChange: (isFirstTerm: Boolean) -> Unit,
    isSubmitEnabled: Boolean,
    onAddGrade: () -> Unit,
    isEditMode: Boolean,
    isDeleted: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TeacherTopBar(
                title = stringResource(R.string.lesson_grade),
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(TeacherActions.delete(onDeleteClick))
                } else {
                    emptyList()
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.small),
            result = gradeTemplateResult,
            isDeleted = isDeleted,
            deletedMessage = stringResource(R.string.lesson_grade_deleted),
        ) {
            FormStatusContent(
                formStatus = formStatus,
                savingText = stringResource(R.string.lesson_saving_grade),
            ) {
                MainContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    lesson = lesson,
                    name = name,
                    onNameChange = onNameChange,
                    description = description,
                    onDescriptionChange = onDescriptionChange,
                    weight = weight,
                    onWeightChange = onWeightChange,
                    isFirstTerm = isFirstTerm,
                    onIsFirstTermChange = onIsFirstTermChange,
                    submitText = if (isEditMode) {
                        stringResource(R.string.lesson_edit_grade)
                    } else {
                        stringResource(R.string.lesson_save_grade)
                    },
                    isSubmitEnabled = isSubmitEnabled,
                    onSubmit = onAddGrade,
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    lesson: Lesson?,
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    description: InputField<String?>,
    onDescriptionChange: (description: String) -> Unit,
    weight: InputField<String>,
    onWeightChange: (weight: String) -> Unit,
    isFirstTerm: Boolean,
    onIsFirstTermChange: (isFirstTerm: Boolean) -> Unit,
    submitText: String,
    isSubmitEnabled: Boolean,
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

        Header(lesson = lesson)
        TermPicker(
            lesson = lesson,
            isFirstTerm = isFirstTerm,
            onIsFirstTermChange = onIsFirstTermChange,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = name,
            onValueChange = { onNameChange(it) },
            label = stringResource(R.string.lesson_name),
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = description,
            onValueChange = { onDescriptionChange(it) },
            label = stringResource(R.string.lesson_description),
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormTextField(
            modifier = textFieldModifier,
            inputField = weight,
            onValueChange = { onWeightChange(it) },
            label = stringResource(R.string.lesson_weight),
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Number),
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

@Composable
private fun Header(
    lesson: Lesson?,
    modifier: Modifier = Modifier,
) {
    val lessonName = lesson?.name.orEmpty()
    val schoolClassName = lesson?.schoolClass?.name.orEmpty()
    Text(
        modifier = modifier,
        text = "$lessonName $schoolClassName",
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
private fun TermPicker(
    lesson: Lesson?,
    isFirstTerm: Boolean,
    onIsFirstTermChange: (isFirstTerm: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard {
        Column(modifier = modifier.selectableGroup()) {
            val schoolYear = lesson?.schoolClass?.schoolYear
            val firstTerm = schoolYear?.firstTerm?.name
                ?: stringResource(R.string.lesson_first_term)
            val secondTerm = schoolYear?.secondTerm?.name
                ?: stringResource(R.string.lesson_second_term)

            Text(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                text = stringResource(R.string.lesson_term_label),
                style = MaterialTheme.typography.labelLarge,
            )

            TeacherRadioButton(
                label = stringResource(R.string.lesson_term, firstTerm),
                selected = isFirstTerm,
                onClick = { onIsFirstTermChange(true) },
            )
            TeacherRadioButton(
                label = stringResource(R.string.lesson_term, secondTerm),
                selected = !isFirstTerm,
                onClick = { onIsFirstTermChange(false) },
            )
        }
    }
}

@Preview
@Composable
private fun GradeTemplateFormScreenPreview(
    @PreviewParameter(LessonPreviewParameterProvider::class, limit = 1) lesson: Lesson
) {
    TeacherTheme {
        Surface {
            var form by remember {
                mutableStateOf(GradeTemplateFormProvider.createDefaultForm(name = "Dodawanie"))
            }

            GradeTemplateFormScreen(
                gradeTemplateResult = Result.Success(null),
                lesson = lesson,
                snackbarHostState = remember { SnackbarHostState() },
                showNavigationIcon = true,
                onNavBack = {},
                formStatus = form.status,
                name = form.name,
                onNameChange = {
                    form = form.copy(name = GradeTemplateFormProvider.validateName(it))
                },
                description = form.description,
                onDescriptionChange = {
                    form =
                        form.copy(description = GradeTemplateFormProvider.validateDescription(it))
                },
                weight = form.weight,
                onWeightChange = {
                    form = form.copy(weight = GradeTemplateFormProvider.validateWeight(it))
                },
                isFirstTerm = form.isFirstTerm,
                onIsFirstTermChange = { form = form.copy(isFirstTerm = it) },
                isSubmitEnabled = form.isSubmitEnabled,
                onAddGrade = {},
                isEditMode = true,
                isDeleted = false,
                onDeleteClick = {},
            )
        }
    }
}