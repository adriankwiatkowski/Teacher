package com.example.teacher.core.ui.component

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.provider.TeacherActions
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IconButtonTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun contentDescriptionExists() {
        rule.apply {
            val action = TeacherActions.add(onClick = {})
            val text = activity.getString(action.text)

            setContent { TeacherIconButton(action) }

            onNodeWithContentDescription(text).assertIsDisplayed()
        }
    }

    @Test
    fun clickable() {
        rule.apply {
            var clicked by mutableStateOf(false)
            val action = TeacherActions.add(onClick = { clicked = true })
            val text = activity.getString(action.text)

            setContent { TeacherIconButton(action) }

            onNodeWithContentDescription(text).performClick()

            assertTrue(clicked)
        }
    }
}