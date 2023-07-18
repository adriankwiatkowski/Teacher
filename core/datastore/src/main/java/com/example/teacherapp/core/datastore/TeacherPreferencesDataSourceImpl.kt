package com.example.teacherapp.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.teacherapp.core.model.data.SettingsData
import com.example.teacherapp.core.model.data.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TeacherPreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : TeacherPreferencesDataSource {

    override val settings: Flow<SettingsData> = dataStore.data.map { settings ->
        val theme = settings[themeKey]?.let { themeId ->
            ThemeConfig.values().firstOrNull { theme -> theme.value == themeId }
        } ?: ThemeConfig.SystemDefault

        val useDynamicColor = settings[useDynamicColorKey] ?: true

        SettingsData(
            themeConfig = theme,
            useDynamicColor = useDynamicColor,
        )
    }

    override suspend fun setTheme(themeConfig: ThemeConfig) {
        dataStore.edit { settings ->
            settings[themeKey] = themeConfig.value
        }
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        dataStore.edit { settings ->
            settings[useDynamicColorKey] = useDynamicColor
        }
    }
}

private val themeKey = intPreferencesKey("theme")
private val useDynamicColorKey = booleanPreferencesKey("use-dynamic-color")