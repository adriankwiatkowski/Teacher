package com.example.teacher.core.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.theme.TeacherTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
class LargeTextTest {

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
            TeacherTheme(dynamicColor = false) {
                TeacherLargeText(text)
            }
        }

        rule.onNodeWithText(text).assertIsDisplayed()
    }

    private val text = "Label"
}