package com.example.teacher.core.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.provider.TeacherActions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(AndroidJUnit4::class)
class TopBarTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun titleExists() {
        rule.setContent {
            TeacherTopBar(
                title = title,
                showNavigationIcon = true,
                onNavigationIconClick = {},
                scrollBehavior = null,
                menuItems = listOf(TeacherActions.delete(onClick = {})),
            )
        }

        rule.onNodeWithText(title).assertExists()
    }

    private val title = "Title"
}