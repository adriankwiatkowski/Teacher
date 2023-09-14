package com.example.teacher.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun TeacherButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: TeacherIcon? = null,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = {
            if (icon != null) {
                Icon(imageVector = icon.icon, contentDescription = null)
            }

            Text(text = label)
        },
    )
}

@Preview
@Composable
private fun TeacherButtonPreview() {
    TeacherTheme {
        Surface {
            TeacherButton(label = "Button", onClick = {})
        }
    }
}

@Preview
@Composable
private fun TeacherButtonWithIconPreview() {
    TeacherTheme {
        Surface {
            TeacherButton(label = "Button", icon = TeacherIcons.chart(), onClick = {})
        }
    }
}