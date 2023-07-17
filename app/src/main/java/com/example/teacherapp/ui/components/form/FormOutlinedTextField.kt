package com.example.teacherapp.ui.components.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.input.InputField

@Composable
fun <T> FormOutlinedTextField(
    inputField: InputField<T>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputToString: (InputField<T>) -> String = { it.value?.toString() ?: "" },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    onLeadingIconClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    TeacherOutlinedTextField(
        value = inputToString(inputField),
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = transformLabel(label, inputField),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        onLeadingIconClick = onLeadingIconClick,
        trailingIcon = when {
            trailingIcon != null -> trailingIcon
            !inputField.isError -> Icons.Default.CheckCircle
            else -> null
        },
        onTrailingIconClick = onTrailingIconClick,
        isError = inputField.shouldShowError,
        supportingText = transformSupportingText(inputField),
        counter = inputField.counter,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

@Composable
fun <T> FormOutlinedTextField(
    inputField: InputField<T>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    inputToString: (InputField<T>) -> String = { it.value?.toString() ?: "" },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    TeacherOutlinedTextField(
        value = inputToString(inputField),
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = transformLabel(label, inputField),
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = inputField.shouldShowError,
        supportingText = transformSupportingText(inputField),
        counter = inputField.counter,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )
}

private fun <T> transformLabel(label: String?, inputField: InputField<T>): String? =
    if (label != null) {
        "$label${if (inputField.isRequired) "*" else ""}"
    } else {
        null
    }

private fun <T> transformSupportingText(
    inputField: InputField<T>
): String? = when {
    inputField.supportingText != null -> inputField.supportingText
    inputField.isRequired -> "Wymagane*"
    else -> null
}

@Preview(showBackground = true)
@Composable
private fun FormOutlinedTextFieldErrorPreview() {
    FormOutlinedTextField(
        inputField = InputField(
            value = "Text",
            isError = true,
            isEdited = true,
            supportingText = "Very very very very very very very very long supportive text",
            counter = 30 to 20,
        ),
        onValueChange = {},
        label = "Label",
    )
}

@Preview(showBackground = true)
@Composable
private fun FormOutlinedTextFieldOkPreview() {
    FormOutlinedTextField(
        inputField = InputField(
            value = "Text",
            supportingText = "Supportive Text",
            counter = 10 to 20,
        ),
        onValueChange = {},
        label = "Label",
    )
}