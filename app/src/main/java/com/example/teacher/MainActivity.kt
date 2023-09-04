package com.example.teacher

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
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.SettingsData
import com.example.teacher.core.model.data.ThemeConfig
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.ui.TeacherApp
import com.example.teacher.ui.rememberTeacherAppState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        setContent {
            val authState by viewModel.authState.collectAsStateWithLifecycle()
            val settingsDataResult by viewModel.settings.collectAsStateWithLifecycle()

            val settingsData =
                remember(settingsDataResult) { getSettingsDataOrDefault(settingsDataResult) }
            val darkTheme = shouldShowDarkTheme(settingsData)

            TeacherTheme(
                darkTheme = darkTheme,
                dynamicColor = settingsData.useDynamicColor,
                tonalElevation = TonalElevation,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    tonalElevation = TonalElevation,
                ) {
                    val appState = rememberTeacherAppState()
                    TeacherApp(
                        appState = appState,
                        isAuthenticated = authState.isAuthenticated,
                        authenticate = ::authenticate,
                        isDeviceSecure = authState.isDeviceSecured,
                        enableAuthentication = enableAuthentication,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        authenticate()
    }

    private fun authenticate() {
        if (!enableAuthentication) {
            return
        }

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
    TeacherTheme {
        Surface {
            TeacherApp(
                isAuthenticated = true,
                authenticate = {},
                isDeviceSecure = true,
                enableAuthentication = true,
            )
        }
    }
}

private val TonalElevation = 5.dp

// Set whether authentication should be enabled in debug mode.
private const val SHOULD_ENABLE_AUTHENTICATION = false

// Don't apply authentication if in debug mode and
// flag SHOULD_ENABLE_AUTHENTICATION is set to disabled.
// In release it will always be enabled.
private val enableAuthentication = !BuildConfig.DEBUG || SHOULD_ENABLE_AUTHENTICATION