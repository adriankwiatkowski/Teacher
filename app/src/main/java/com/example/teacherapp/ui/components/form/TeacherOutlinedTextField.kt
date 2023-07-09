package com.example.teacherapp.ui.components.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TeacherOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    onLeadingIconClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    counter: Pair<Int, Int>? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    val leadingIconComposable: @Composable (() -> Unit)? = if (leadingIcon != null) {
        @Composable {
            val clickModifier = if (onLeadingIconClick != null) {
                Modifier.clickable(onClick = onLeadingIconClick)
            } else {
                Modifier
            }

            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .then(clickModifier),
                imageVector = leadingIcon,
                contentDescription = null,
            )
        }
    } else {
        null
    }

    val trailingIconComposable: @Composable (() -> Unit)? = when {
        isError -> {
            @Composable {
                val clickModifier = if (onTrailingIconClick != null) {
                    Modifier.clickable(onClick = onTrailingIconClick)
                } else {
                    Modifier
                }

                Icon(
                    modifier = clickModifier
                        .padding(8.dp),
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colors.error,
                )
            }
        }
        trailingIcon != null -> {
            @Composable {
                val clickModifier = if (onTrailingIconClick != null) {
                    Modifier.clickable(onClick = onTrailingIconClick)
                } else {
                    Modifier
                }

                Icon(
                    modifier = clickModifier
                        .padding(8.dp),
                    imageVector = trailingIcon,
                    contentDescription = null,
                )
            }
        }
        else -> null
    }

    TeacherOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIconComposable,
        trailingIcon = trailingIconComposable,
        isError = isError,
        supportingText = supportingText,
        counter = counter,
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
fun TeacherOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    counter: Pair<Int, Int>? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    val labelComposable: @Composable (() -> Unit)? = if (label != null) {
        @Composable {
            Text(
                text = label,
                color = if (isError) MaterialTheme.colors.error else Color.Unspecified,
            )
        }
    } else {
        null
    }

    val placeholderComposable: @Composable (() -> Unit)? = if (placeholder != null) {
        @Composable { Text(placeholder) }
    } else {
        null
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = labelComposable,
            placeholder = placeholderComposable,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            val textColor = if (isError) MaterialTheme.colors.error else Color.Unspecified

            if (supportingText != null) {
                Text(
                    modifier = Modifier
                        .weight(3f)
                        .padding(start = 16.dp),
                    text = supportingText,
                    color = textColor,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.caption,
                )
            }

            if (counter != null) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    text = "${counter.first}/${counter.second}",
                    color = textColor,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TeacherOutlinedTextFieldErrorPreview() {
    TeacherOutlinedTextField(
        value = "Text",
        onValueChange = {},
        label = "Label",
        isError = true,
        supportingText = "Very very very very very very very very long supportive text",
        counter = 30 to 20,
    )
}

@Preview(showBackground = true)
@Composable
private fun TeacherOutlinedTextFieldOkPreview() {
    TeacherOutlinedTextField(
        value = "Text",
        onValueChange = {},
        label = "Label",
        supportingText = "Supportive Text",
        counter = 10 to 20,
    )
}

@Preview(showBackground = true)
@Composable
private fun TeacherOutlinedTextFieldCounterPreview() {
    TeacherOutlinedTextField(
        value = "Text",
        onValueChange = {},
        label = "Label",
        counter = 10 to 20,
    )
}