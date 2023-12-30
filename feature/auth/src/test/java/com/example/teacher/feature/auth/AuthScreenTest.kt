package com.example.teacher.feature.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun init() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun textVisible() {
        val sensitiveData = rule.activity.getString(R.string.auth_sensitive_data_explanation)
        val notSecured = rule.activity.getString(R.string.auth_device_not_secure)
        val buttonText = rule.activity.getString(R.string.auth_unlock_app)
        rule.setContent { AuthScreen(authenticate = {}, isDeviceSecure = true) }

        rule.onNodeWithText(sensitiveData).assertExists()
        rule.onNodeWithText(buttonText).assertExists().assertHasClickAction()
        rule.onNodeWithText(notSecured).assertDoesNotExist()
    }

    @Test
    fun deviceNotSecuredTextVisible() {
        val sensitiveData = rule.activity.getString(R.string.auth_sensitive_data_explanation)
        val notSecured = rule.activity.getString(R.string.auth_device_not_secure)
        val buttonText = rule.activity.getString(R.string.auth_unlock_app)
        rule.setContent { AuthScreen(authenticate = {}, isDeviceSecure = false) }

        rule.onNodeWithText(sensitiveData).assertExists()
        rule.onNodeWithText(buttonText).assertExists().assertHasClickAction()
        rule.onNodeWithText(notSecured).assertExists()
    }
}