package com.example.teacher.core.ui.component.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.component.TeacherTextField
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing

@Composable
fun <T> FormTextField(
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
    prefix: String? = null,
    suffix: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    TeacherTextField(
        value = inputToString(inputField),
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label(label, inputField),
        placeholder = placeholder(placeholder),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon(trailingIcon, inputField),
        prefix = prefix(prefix),
        suffix = suffix(suffix),
        supportingText = supportingText(inputField),
        isError = inputField.shouldShowError,
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

private fun <T> label(label: String?, inputField: InputField<T>): (@Composable () -> Unit)? {
    return if (label != null) {
        { Text("$label${if (inputField.isRequired) "*" else ""}") }
    } else {
        null
    }
}

private fun placeholder(placeholder: String?): (@Composable () -> Unit)? {
    return if (placeholder != null) {
        { Text(placeholder) }
    } else {
        null
    }
}

private fun <T> trailingIcon(
    trailingIcon: @Composable (() -> Unit)?,
    inputField: InputField<T>,
): (@Composable () -> Unit)? {
    return if (trailingIcon == null && inputField.isValid) {
        {
            val icon = TeacherIcons.valid()
            Icon(imageVector = icon.icon, contentDescription = stringResource(icon.text))
        }
    } else {
        trailingIcon
    }
}

private fun prefix(prefix: String?): (@Composable () -> Unit)? {
    return if (prefix != null) {
        { Text(prefix) }
    } else {
        null
    }
}

private fun suffix(suffix: String?): (@Composable () -> Unit)? {
    return if (suffix != null) {
        { Text(suffix) }
    } else {
        null
    }
}

@Composable
private fun <T> supportingText(inputField: InputField<T>): (@Composable () -> Unit)? {
    if (inputField.supportingText == null && !inputField.isRequired) {
        return null
    }

    return {
        val isError = inputField.shouldShowError
        val counter = inputField.counter
        val supportingText = if (inputField.supportingText == null && isError) {
            R.string.required
        } else {
            inputField.supportingText
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            val textColor = if (isError) MaterialTheme.colorScheme.error else Color.Unspecified

            if (supportingText != null) {
                Text(
                    modifier = Modifier
                        .weight(3f)
                        .padding(start = MaterialTheme.spacing.large),
                    text = stringResource(supportingText),
                    color = textColor,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.labelSmall,
                )
            }

            if (counter != null) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = MaterialTheme.spacing.large),
                    text = "${counter.first}/${counter.second}",
                    color = textColor,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FormTextFieldErrorPreview() {
    TeacherTheme {
        Surface {
            FormTextField(
                inputField = InputField(
                    value = "Text",
                    isError = true,
                    isEdited = true,
                    supportingText = R.string.very_long_text,
                    counter = 30 to 20,
                ),
                onValueChange = {},
                label = "Label",
            )
        }
    }
}

@Preview
@Composable
private fun FormTextFieldOkPreview() {
    TeacherTheme {
        Surface {
            FormTextField(
                inputField = InputField(
                    value = "Text",
                    supportingText = R.string.supportive_text,
                    counter = 10 to 20,
                ),
                onValueChange = {},
                label = "Label",
            )
        }
    }
}