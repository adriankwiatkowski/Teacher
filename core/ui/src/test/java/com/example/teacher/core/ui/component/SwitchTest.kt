package com.example.teacher.core.ui.component

import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
class SwitchTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun isToggleable() {
        rule.setContent { TeacherSwitch(label = "Label", checked = true, onCheckedChange = {}) }

        rule.onNodeWithText("Label")
            .assertExists()
            .assertIsToggleable()
    }
}