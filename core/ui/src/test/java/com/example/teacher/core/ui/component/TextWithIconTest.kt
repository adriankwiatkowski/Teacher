package com.example.teacher.core.ui.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.provider.TeacherIcons
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextWithIconTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun labelExists() {
        rule.setContent { TextWithIcon(text = label, icon = icon) }

        rule.onNodeWithText(label).assertExists()
    }

    @Test
    fun iconExists() {
        rule.setContent { TextWithIcon(text = label, icon = icon) }

        rule.onNodeWithContentDescription(rule.activity.getString(icon.text))
            .assertDoesNotExist()
    }

    private val label = "Label"
    private val icon = TeacherIcons.add()
}