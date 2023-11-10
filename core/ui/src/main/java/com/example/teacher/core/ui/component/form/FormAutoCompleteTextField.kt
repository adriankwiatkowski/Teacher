package com.example.teacher.core.ui.component.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.theme.TeacherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormAutoCompleteTextField(
    inputField: InputField<T>,
    onValueChange: (String) -> Unit,
    onSuggestionSelect: (T) -> Unit,
    suggestions: List<T>,
    modifier: Modifier = Modifier,
    inputToString: (InputField<T>) -> String = { it.value?.toString() ?: "" },
    suggestionToString: (T) -> String = { it?.toString() ?: "" },
    enabled: Boolean = true,
    readOnly: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small.copy(
        bottomEnd = ZeroCornerSize,
        bottomStart = ZeroCornerSize
    ),
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors(),
) {
    var expanded by remember { mutableStateOf(false) }

    FormAutoCompleteTextField(
        inputField = inputField,
        onValueChange = onValueChange,
        onSuggestionSelect = onSuggestionSelect,
        suggestions = suggestions,
        expanded = expanded,
        setExpanded = { expanded = it },
        toggleExpanded = { expanded = !expanded },
        modifier = modifier,
        inputToString = inputToString,
        suggestionToString = suggestionToString,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormAutoCompleteTextField(
    inputField: InputField<T>,
    onValueChange: (String) -> Unit,
    onSuggestionSelect: (T) -> Unit,
    suggestions: List<T>,
    expanded: Boolean,
    setExpanded: (expanded: Boolean) -> Unit,
    toggleExpanded: () -> Unit,
    modifier: Modifier = Modifier,
    inputToString: (InputField<T>) -> String = { it.value?.toString() ?: "" },
    suggestionToString: (T) -> String = { it?.toString() ?: "" },
    enabled: Boolean = true,
    readOnly: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = ExposedDropdownMenuDefaults.textFieldColors(),
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { toggleExpanded() },
    ) {
        FormTextField(
            inputField = inputField,
            onValueChange = onValueChange,
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = modifier.menuAnchor(),
            inputToString = inputToString,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
                ?: { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
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

        val filterSuggestions = remember(inputField.value, suggestions, readOnly) {
            val input = inputToString(inputField).trim()
            if (readOnly || input.isEmpty()) {
                return@remember suggestions
            }

            suggestions.filter { suggestion ->
                suggestionToString(suggestion).contains(input, ignoreCase = true)
            }
        }

        if (filterSuggestions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) },
            ) {
                filterSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(text = suggestionToString(suggestion)) },
                        onClick = {
                            onSuggestionSelect(suggestion)
                            setExpanded(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AutoCompleteTextFieldPreview() {
    TeacherTheme {
        Surface {
            var value by remember { mutableStateOf("1") }
            FormAutoCompleteTextField(
                inputField = InputField(value),
                onValueChange = { value = it },
                onSuggestionSelect = { value = it },
                suggestions = listOf("111", "112", "113"),
                readOnly = false,
            )
        }
    }
}

@Preview
@Composable
private fun AutoCompleteTextFieldReadOnlyPreview() {
    TeacherTheme {
        Surface {
            var value by remember { mutableStateOf("111") }
            FormAutoCompleteTextField(
                inputField = InputField(value),
                onValueChange = { value = it },
                onSuggestionSelect = { value = it },
                suggestions = listOf("111", "112", "113"),
            )
        }
    }
}