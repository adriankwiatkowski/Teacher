package com.example.teacher.core.ui.component

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.R
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DiscardDialogTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun buttonsClickable() {
        var cancelClicked by mutableStateOf(false)
        var discardClicked by mutableStateOf(false)

        rule.setContent {
            TeacherDiscardDialog(
                onDismissRequest = { cancelClicked = true },
                onConfirmClick = { discardClicked = true },
            )
        }
        val cancelString = rule.activity.getString(R.string.ui_cancel)
        val discardString = rule.activity.getString(R.string.ui_discard)

        rule.onNodeWithText(cancelString).performClick()
        rule.onNodeWithText(discardString).performClick()

        assertTrue(cancelClicked)
        assertTrue(discardClicked)
    }
}