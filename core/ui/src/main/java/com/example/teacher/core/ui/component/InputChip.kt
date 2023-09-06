package com.example.teacher.core.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SelectableChipBorder
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.theme.TeacherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherInputChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    avatar: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = InputChipDefaults.shape,
    colors: SelectableChipColors = InputChipDefaults.inputChipColors(),
    elevation: SelectableChipElevation? = InputChipDefaults.inputChipElevation(),
    border: SelectableChipBorder? = InputChipDefaults.inputChipBorder(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    InputChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        modifier = modifier,
        enabled = enabled,
        leadingIcon = leadingIcon,
        avatar = avatar,
        trailingIcon = trailingIcon,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TeacherInputChipPreview() {
    TeacherTheme {
        Surface {
            Column {
                TeacherInputChip(label = "Chip selected", selected = true, onClick = {})
                TeacherInputChip(label = "Chip not selected", selected = false, onClick = {})
            }
        }
    }
}