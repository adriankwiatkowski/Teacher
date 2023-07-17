package com.example.teacherapp.ui.components.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.theme.TeacherAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> FormAutoCompleteOutlinedTextField(
    inputField: InputField<T>,
    onValueChange: (String) -> Unit,
    onSuggestionSelect: (T) -> Unit,
    suggestions: List<T>,
    modifier: Modifier = Modifier,
    inputToString: (InputField<T>) -> String = { it.value?.toString() ?: "" },
    suggestionToString: (T) -> String = { it?.toString() ?: "" },
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
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

    FormAutoCompleteOutlinedTextField(
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
fun <T> FormAutoCompleteOutlinedTextField(
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
    readOnly: Boolean = false,
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
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { toggleExpanded() },
    ) {
        FormOutlinedTextField(
            inputField = inputField,
            onValueChange = onValueChange,
            modifier = modifier,
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
                        onClick = {
                            onSuggestionSelect(suggestion)
                            setExpanded(false)
                        },
                        text = { Text(text = suggestionToString(suggestion)) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AutoCompleteTextFieldPreview() {
    TeacherAppTheme {
        var value by remember { mutableStateOf("1") }
        FormAutoCompleteOutlinedTextField(
            inputField = InputField(""),
            onValueChange = { value = it },
            onSuggestionSelect = { value = it },
            suggestions = listOf("111", "112", "113"),
        )
    }
}