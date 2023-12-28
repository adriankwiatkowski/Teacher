package com.example.teacher.core.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
class InputChipTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun labelExists() {
        rule.apply {
            setContent {
                TeacherInputChip(
                    label = label,
                    selected = true,
                    onClick = {},
                )
            }

            onNodeWithText(label).assertIsDisplayed()
        }
    }

    @Test
    fun clickable() {
        rule.apply {
            var clicked by mutableStateOf(false)
            setContent {
                TeacherInputChip(
                    label = label,
                    selected = true,
                    onClick = { clicked = true },
                )
            }

            onNodeWithText(label).performClick()

            assertTrue(clicked)
        }
    }

    private val label = "Label"
}