package com.example.teacher.core.data.repository.settings

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.SettingsData
import com.example.teacher.core.model.data.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val settings: Flow<Result<SettingsData>>

    suspend fun setTheme(themeConfig: ThemeConfig)

    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
}