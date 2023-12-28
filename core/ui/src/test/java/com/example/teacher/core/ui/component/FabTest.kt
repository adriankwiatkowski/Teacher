package com.example.teacher.core.ui.component

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.provider.TeacherActions
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
class FabTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun fab_contentDescriptionExists() {
        val action = TeacherActions.add(onClick = {})
        val text = rule.activity.getString(action.text)
        rule.setContent { TeacherFab(action) }

        rule.onNodeWithContentDescription(text).assertIsDisplayed()
    }

    @Test
    fun fab_clickable() {
        var clicked by mutableStateOf(false)
        val action = TeacherActions.add(onClick = { clicked = true })
        val text = rule.activity.getString(action.text)
        rule.setContent { TeacherFab(action) }

        rule.onNodeWithContentDescription(text).performClick()

        assertTrue(clicked)
    }

    @Test
    fun extendedFab_contentDescriptionExists() {
        val action = TeacherActions.add(onClick = {})
        val text = rule.activity.getString(action.text)
        rule.setContent { TeacherExtendedFab(action) }

        rule.onNodeWithContentDescription(text).assertIsDisplayed()
    }

    @Test
    fun extendedFab_labelExists() {
        val action = TeacherActions.add(onClick = {})
        val text = rule.activity.getString(action.text)
        rule.setContent { TeacherExtendedFab(action) }

        rule.onNodeWithText(text, useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun extendedFab_clickable() {
        var clicked by mutableStateOf(false)
        val action = TeacherActions.add(onClick = { clicked = true })
        val text = rule.activity.getString(action.text)
        rule.setContent { TeacherExtendedFab(action) }

        rule.onNodeWithContentDescription(text).performClick()

        assertTrue(clicked)
    }
}