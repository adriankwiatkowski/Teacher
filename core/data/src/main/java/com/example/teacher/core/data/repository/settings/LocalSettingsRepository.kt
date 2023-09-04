package com.example.teacher.core.data.repository.settings

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.datastore.TeacherPreferencesDataSource
import com.example.teacher.core.model.data.SettingsData
import com.example.teacher.core.model.data.ThemeConfig
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LocalSettingsRepository @Inject constructor(
    private val dataSource: TeacherPreferencesDataSource,
) : SettingsRepository {

    override val settings: Flow<Result<SettingsData>>
        get() = dataSource.settings.asResult()

    override suspend fun setTheme(themeConfig: ThemeConfig) {
        dataSource.setTheme(themeConfig)
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        dataSource.setUseDynamicColor(useDynamicColor)
    }
}