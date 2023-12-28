package com.example.teacher.core.ui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ButtonTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun labelExists() {
        rule.setContent { TeacherButton(label = label, onClick = {}) }

        rule.onNodeWithText(label).assertExists()
    }

    @Test
    fun clickable() {
        var counter by mutableIntStateOf(0)
        rule.setContent { TeacherButton(label = "$label $counter", onClick = { counter++ }) }

        rule.onNodeWithText("$label 0").performClick()

        rule.onNodeWithText("$label 1").assertExists()
    }

    private val label = "Label"
}