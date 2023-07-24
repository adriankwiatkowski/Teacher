package com.example.teacherapp.feature.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
internal fun SettingsRoute(viewModel: SettingsViewModel = hiltViewModel()) {
    val settingsDataResult by viewModel.settingsData.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsDataResult = settingsDataResult,
        onThemeChange = viewModel::onThemeChange,
        onDynamicColorChange = viewModel::onDynamicColorChange,
    )
}