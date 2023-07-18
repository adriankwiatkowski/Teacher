package com.example.teacherapp.core.datastore

import com.example.teacherapp.core.model.data.SettingsData
import com.example.teacherapp.core.model.data.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface TeacherPreferencesDataSource {

    val settings: Flow<SettingsData>

    suspend fun setTheme(themeConfig: ThemeConfig)

    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
}