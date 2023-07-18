package com.example.teacherapp.ui.screens.gradetemplate

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.GradeTemplate
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.components.TeacherTopBarDefaults
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.form.FormStatusContent
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.result.ResultContent
import com.example.teacherapp.ui.screens.gradetemplate.data.GradeTemplateFormProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeTemplateFormScreen(
    gradeTemplateResult: Result<GradeTemplate?>,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    formStatus: FormStatus,
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    description: InputField<String?>,
    onDescriptionChange: (description: String) -> Unit,
    weight: InputField<String>,
    onWeightChange: (weight: String) -> Unit,
    isSubmitEnabled: Boolean,
    onAddGrade: () -> Unit,
    isEditMode: Boolean,
    isDeleted: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollBehavior = TeacherTopBarDefaults.default()

    Scaffold(
        modifier = modifier,
        topBar = {
            TeacherTopBar(
                title = "Ocena",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = if (isEditMode) {
                    listOf(ActionMenuItemProvider.delete(onDeleteClick))
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
            deletedMessage = "Usunięto ocenę",
        ) {
            FormStatusContent(
                formStatus = formStatus,
                savingText = "Zapisywanie oceny...",
            ) {
                MainContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    name = name,
                    onNameChange = onNameChange,
                    description = description,
                    onDescriptionChange = onDescriptionChange,
                    weight = weight,
                    onWeightChange = onWeightChange,
                    submitText = if (isEditMode) "Edytuj ocenę" else "Dodaj ocenę",
                    isSubmitEnabled = isSubmitEnabled,
                    onSubmit = onAddGrade,
                )
            }
        }
    }
}

@Composable
private fun MainContent(
    name: InputField<String>,
    onNameChange: (name: String) -> Unit,
    description: InputField<String?>,
    onDescriptionChange: (description: String) -> Unit,
    weight: InputField<String>,
    onWeightChange: (weight: String) -> Unit,
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

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = name,
            onValueChange = { onNameChange(it) },
            label = "Nazwa",
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = description,
            onValueChange = { onDescriptionChange(it) },
            label = "Opis",
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )

        FormOutlinedTextField(
            modifier = textFieldModifier,
            inputField = weight,
            onValueChange = { onWeightChange(it) },
            label = "Waga",
            keyboardOptions = commonKeyboardOptions.copy(keyboardType = KeyboardType.Number),
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
private fun GradeTemplateFormScreenPreview() {
    TeacherAppTheme {
        Surface {
            var form by remember {
                mutableStateOf(GradeTemplateFormProvider.createDefaultForm(name = "Dodawanie"))
            }

            GradeTemplateFormScreen(
                gradeTemplateResult = Result.Success(null),
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
                isSubmitEnabled = form.isSubmitEnabled,
                onAddGrade = {},
                isEditMode = true,
                isDeleted = false,
                onDeleteClick = {},
            )
        }
    }
}