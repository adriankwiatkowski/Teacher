package com.example.teacherapp.ui.screens.settings.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.teacherapp.core.model.data.ThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    var themeConfig by mutableStateOf(ThemeConfig.SystemDefault)
        private set

    var useDynamicColor by mutableStateOf(true)

    fun onThemeChange(themeConfig: ThemeConfig) {
        this.themeConfig = themeConfig
    }

    fun onDynamicColorChange(useDynamicColor: Boolean) {
        this.useDynamicColor = useDynamicColor
    }
}