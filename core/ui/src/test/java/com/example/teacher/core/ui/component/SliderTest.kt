package com.example.teacher.core.ui.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
class SliderTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun textIsDisplayed() {
        rule.setContent {
            TeacherIntSlider(
                label = "Label",
                min = 0,
                max = 100,
                value = 50,
                onValueChange = {},
            )
        }

        rule.onNodeWithText("Label").assertExists()
    }
}