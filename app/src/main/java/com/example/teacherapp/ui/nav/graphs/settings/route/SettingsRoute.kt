package com.example.teacherapp.ui.nav.graphs.settings.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.settings.SettingsScreen
import com.example.teacherapp.ui.screens.settings.data.SettingsViewModel

@Composable
internal fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val settingsDataResult by viewModel.settingsData.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsDataResult = settingsDataResult,
        onThemeChange = viewModel::onThemeChange,
        onDynamicColorChange = viewModel::onDynamicColorChange,
    )
}