package com.example.teacherapp.feature.settings.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.feature.settings.SettingsScreen
import com.example.teacherapp.feature.settings.SettingsViewModel

@Composable
internal fun SettingsRoute(
    snackbarHostState: SnackbarHostState,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsDataResult by viewModel.settingsData.collectAsStateWithLifecycle()

    SettingsScreen(
        settingsDataResult = settingsDataResult,
        snackbarHostState = snackbarHostState,
        onThemeChange = viewModel::onThemeChange,
        onDynamicColorChange = viewModel::onDynamicColorChange,
    )
}