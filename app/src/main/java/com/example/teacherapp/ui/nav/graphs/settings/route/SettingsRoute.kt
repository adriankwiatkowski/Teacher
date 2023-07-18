package com.example.teacherapp.ui.nav.graphs.settings.route

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.teacherapp.ui.screens.settings.SettingsScreen
import com.example.teacherapp.ui.screens.settings.data.SettingsViewModel

@Composable
internal fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    SettingsScreen(
        theme = viewModel.themeConfig,
        onThemeChange = viewModel::onThemeChange,
        useDynamicColor = viewModel.useDynamicColor,
        onDynamicColorChange = viewModel::onDynamicColorChange,
    )
}