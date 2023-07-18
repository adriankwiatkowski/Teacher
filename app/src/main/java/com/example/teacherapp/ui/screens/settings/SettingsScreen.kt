package com.example.teacherapp.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.model.data.ThemeConfig
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing
import com.example.teacherapp.ui.theme.supportsDynamicTheming

@Composable
fun SettingsScreen(
    theme: ThemeConfig,
    onThemeChange: (theme: ThemeConfig) -> Unit,
    useDynamicColor: Boolean,
    onDynamicColorChange: (useDynamicColor: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
) {
    Column(modifier = modifier.padding(MaterialTheme.spacing.medium)) {
        Text(
            text = "Ustawienia",
            style = MaterialTheme.typography.titleLarge,
        )

        Column(Modifier.verticalScroll(rememberScrollState())) {
            SectionTitle(text = "Motyw")
            Column(Modifier.selectableGroup()) {
                SettingsRadioButton(
                    label = "Ustawienie domyślne systemu",
                    selected = theme == ThemeConfig.SystemDefault,
                    onClick = { onThemeChange(ThemeConfig.SystemDefault) },
                )
                SettingsRadioButton(
                    label = "Jasny",
                    selected = theme == ThemeConfig.Light,
                    onClick = { onThemeChange(ThemeConfig.Light) },
                )
                SettingsRadioButton(
                    label = "Ciemny",
                    selected = theme == ThemeConfig.Dark,
                    onClick = { onThemeChange(ThemeConfig.Dark) },
                )
            }

            if (supportDynamicColor) {
                SectionTitle(text = "Dynamiczny motyw")
                SettingsSwitch(
                    label = "Używaj dynamicznych kolorów",
                    checked = useDynamicColor,
                    onCheckedChange = onDynamicColorChange,
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(
            top = MaterialTheme.spacing.medium,
            bottom = MaterialTheme.spacing.small,
        ),
        text = text,
        style = MaterialTheme.typography.titleMedium,
    )
}

@Composable
private fun SettingsRadioButton(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(Modifier.width(MaterialTheme.spacing.small))
        Text(label)
    }
}

@Composable
private fun SettingsSwitch(
    label: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (isChecked: Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = onCheckedChange,
            )
            .padding(MaterialTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(modifier = Modifier.weight(1f), text = label)
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    TeacherAppTheme {
        Surface {
            SettingsScreen(
                theme = ThemeConfig.SystemDefault,
                onThemeChange = {},
                useDynamicColor = true,
                onDynamicColorChange = {},
            )
        }
    }
}