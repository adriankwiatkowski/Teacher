package com.example.teacherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.SettingsData
import com.example.teacherapp.core.model.data.ThemeConfig
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.TeacherApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = hiltViewModel<MainActivityViewModel>()
            val settingsDataResult by viewModel.settings.collectAsStateWithLifecycle()

            val settingsData =
                remember(settingsDataResult) { getSettingsDataOrDefault(settingsDataResult) }
            val darkTheme = shouldShowDarkTheme(settingsData)

            TeacherAppTheme(
                darkTheme = darkTheme,
                dynamicColor = settingsData.useDynamicColor,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TeacherApp()
                }
            }
        }
    }
}

private fun getSettingsDataOrDefault(settingsDataResult: Result<SettingsData>): SettingsData {
    return (settingsDataResult as? Result.Success)?.data ?: SettingsData(
        themeConfig = ThemeConfig.FollowSystem,
        useDynamicColor = true,
    )
}

@Composable
private fun shouldShowDarkTheme(settingsData: SettingsData): Boolean {
    return when (settingsData.themeConfig) {
        ThemeConfig.FollowSystem -> isSystemInDarkTheme()
        ThemeConfig.Light -> false
        ThemeConfig.Dark -> true
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    TeacherAppTheme {
        TeacherApp()
    }
}