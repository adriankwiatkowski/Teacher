package com.example.teacher.core.ui.component

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TextFieldTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun labelExists() {
        rule.setContent { TeacherTextField(value = "", onValueChange = {}) }

        rule.onNodeWithText("").assertExists()
    }

    @Test
    fun inputWorks() {
        var value by mutableStateOf("")
        rule.setContent { TeacherTextField(value = value, onValueChange = { value = it }) }

        rule.onNodeWithText("").performTextInput("Test")

        rule.onNodeWithText("Test").assertExists()
    }
}