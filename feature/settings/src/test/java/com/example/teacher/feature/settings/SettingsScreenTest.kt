package com.example.teacher.feature.settings

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.theme.supportsDynamicTheming
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun theme() {
        val themeHeaderMatcher = hasText(rule.activity.getString(R.string.theme))
        val defaultThemeMatcher = hasText(rule.activity.getString(R.string.system_default))
        val lightThemeMatcher = hasText(rule.activity.getString(R.string.light))
        val darkThemeMatcher = hasText(rule.activity.getString(R.string.dark))
        val viewModel = ViewModelProvider(rule.activity)[SettingsViewModel::class.java]
        rule.setContent { SettingsScreen(viewModel) }

        rule.onRoot().printToLog("Settings")

        rule.onNode(themeHeaderMatcher).assertExists()
        rule.onNode(defaultThemeMatcher).assertExists()
        rule.onNode(lightThemeMatcher).assertExists().performClick()
        rule.onNode(darkThemeMatcher).assertExists()
    }

    @Test
    fun dynamicColorsAvailable() {
        val headerMatcher = hasText(rule.activity.getString(R.string.dynamic_theme))
        val switchMatcher = hasText(rule.activity.getString(R.string.use_dynamic_theme))
        val viewModel = ViewModelProvider(rule.activity)[SettingsViewModel::class.java]
        rule.setContent { SettingsScreen(viewModel, supportDynamicColor = true) }

        rule.onRoot().printToLog("Settings")

        rule.onNode(headerMatcher).assertExists()
        rule.onNode(switchMatcher).assertExists().performClick()
    }

    @Test
    fun dynamicColorsNotAvailable() {
        val headerMatcher = hasText(rule.activity.getString(R.string.dynamic_theme))
        val switchMatcher = hasText(rule.activity.getString(R.string.use_dynamic_theme))
        val viewModel = ViewModelProvider(rule.activity)[SettingsViewModel::class.java]
        rule.setContent { SettingsScreen(viewModel, supportDynamicColor = false) }

        rule.onRoot().printToLog("Settings")

        rule.onNode(headerMatcher).assertDoesNotExist()
        rule.onNode(switchMatcher).assertDoesNotExist()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun SettingsScreen(
        viewModel: SettingsViewModel,
        supportDynamicColor: Boolean = supportsDynamicTheming(),
    ) {
        val settingsDataResult by viewModel.settingsData.collectAsStateWithLifecycle()
        SettingsScreen(
            settingsDataResult = settingsDataResult,
            snackbarHostState = remember { SnackbarHostState() },
            onThemeChange = viewModel::onThemeChange,
            onDynamicColorChange = viewModel::onDynamicColorChange,
            supportDynamicColor = supportDynamicColor,
        )
    }
}