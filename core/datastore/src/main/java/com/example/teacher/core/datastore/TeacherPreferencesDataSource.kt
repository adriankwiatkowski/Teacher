package com.example.teacher.core.datastore

import com.example.teacher.core.model.data.SettingsData
import com.example.teacher.core.model.data.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface TeacherPreferencesDataSource {

    val settings: Flow<SettingsData>

    suspend fun setTheme(themeConfig: ThemeConfig)

    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
}