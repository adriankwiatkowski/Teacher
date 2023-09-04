package com.example.teacher.core.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.icon.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun TeacherNavigationBar(
    visible: Boolean,
    modifier: Modifier = Modifier,
    bars: @Composable RowScope.() -> Unit,
) {
//    AnimatedVisibility(
//        modifier = modifier,
//        visible = visible,
//        enter = fadeIn(),
//        exit = fadeOut(),
//    ) {
//    }
    if (!visible) {
        return
    }

    NavigationBar(modifier = modifier) {
        bars()
    }
}

@Composable
fun RowScope.TeacherNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    iconContentDescription: String?,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    colors: NavigationBarItemColors = NavigationBarItemDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(imageVector = icon, contentDescription = iconContentDescription) },
        modifier = modifier,
        enabled = enabled,
        label = { Text(label) },
        alwaysShowLabel = alwaysShowLabel,
        colors = colors,
        interactionSource = interactionSource,
    )
}

@Preview
@Composable
private fun TeacherNavigationBarPreview() {
    TeacherTheme {
        Surface {
            TeacherNavigationBar(visible = true) {
                TeacherNavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = TeacherIcons.Add,
                    iconContentDescription = null,
                    label = "Tab 1",
                )
                TeacherNavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = TeacherIcons.Add,
                    iconContentDescription = null,
                    label = "Tab 2",
                )
                TeacherNavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = TeacherIcons.Add,
                    iconContentDescription = null,
                    label = "Tab 3",
                )
            }
        }
    }
}