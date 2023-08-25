package com.example.teacherapp

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.SettingsData
import com.example.teacherapp.core.model.data.ThemeConfig
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.feature.auth.AuthScreen
import com.example.teacherapp.ui.TeacherApp
import com.example.teacherapp.ui.rememberTeacherAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE,
        )

        setContent {
            val authState by viewModel.authState.collectAsStateWithLifecycle()
            val settingsDataResult by viewModel.settings.collectAsStateWithLifecycle()

            val settingsData =
                remember(settingsDataResult) { getSettingsDataOrDefault(settingsDataResult) }
            val darkTheme = shouldShowDarkTheme(settingsData)

            TeacherAppTheme(
                darkTheme = darkTheme,
                dynamicColor = settingsData.useDynamicColor,
                tonalElevation = TonalElevation,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    tonalElevation = TonalElevation,
                ) {
                    val appState = rememberTeacherAppState()
                    if (authState.isAuthenticated) {
                        // TODO: Fix tabs not preserving state.
                        TeacherApp(appState = appState)
                    } else {
                        AuthScreen(
                            modifier = Modifier.fillMaxSize(),
                            authenticate = { viewModel.authenticate(this@MainActivity) },
                            isDeviceSecure = authState.isDeviceSecured,
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.authenticate(activity = this)
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

@Preview
@Composable
private fun DefaultPreview() {
    TeacherAppTheme {
        Surface {
            TeacherApp()
        }
    }
}

private val TonalElevation = 5.dp