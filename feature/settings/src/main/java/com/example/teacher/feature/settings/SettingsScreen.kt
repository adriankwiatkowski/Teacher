package com.example.teacher.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.SettingsData
import com.example.teacher.core.model.data.ThemeConfig
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.component.TeacherSwitch
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.core.ui.theme.supportsDynamicTheming

@Composable
internal fun SettingsScreen(
    settingsDataResult: Result<SettingsData>,
    snackbarHostState: SnackbarHostState,
    onThemeChange: (theme: ThemeConfig) -> Unit,
    onDynamicColorChange: (useDynamicColor: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        ResultContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.medium),
            result = settingsDataResult,
        ) { settingsData ->
            SettingsPanel(
                theme = settingsData.themeConfig,
                onThemeChange = onThemeChange,
                useDynamicColor = settingsData.useDynamicColor,
                onDynamicColorChange = onDynamicColorChange,
                supportDynamicColor = supportDynamicColor,
            )
        }
    }
}

@Composable
private fun SettingsPanel(
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
                TeacherRadioButton(
                    label = "Ustawienie domyślne systemu",
                    selected = theme == ThemeConfig.FollowSystem,
                    onClick = { onThemeChange(ThemeConfig.FollowSystem) },
                )
                TeacherRadioButton(
                    label = "Jasny",
                    selected = theme == ThemeConfig.Light,
                    onClick = { onThemeChange(ThemeConfig.Light) },
                )
                TeacherRadioButton(
                    label = "Ciemny",
                    selected = theme == ThemeConfig.Dark,
                    onClick = { onThemeChange(ThemeConfig.Dark) },
                )
            }

            if (supportDynamicColor) {
                SectionTitle(text = "Dynamiczny motyw")
                TeacherSwitch(
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

@Preview
@Composable
private fun SettingsScreenPreview() {
    TeacherTheme {
        Surface {
            var settingsData by remember {
                mutableStateOf(
                    SettingsData(
                        themeConfig = ThemeConfig.FollowSystem,
                        useDynamicColor = true,
                    )
                )
            }

            SettingsScreen(
                settingsDataResult = Result.Success(settingsData),
                snackbarHostState = remember { SnackbarHostState() },
                onThemeChange = { settingsData = settingsData.copy(themeConfig = it) },
                onDynamicColorChange = { settingsData = settingsData.copy(useDynamicColor = it) },
            )
        }
    }
}