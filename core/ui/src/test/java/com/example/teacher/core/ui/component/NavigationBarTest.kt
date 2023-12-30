package com.example.teacher.core.ui.component

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.provider.TeacherIcons
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
class NavigationBarTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun childrenExist() {
        val bars = 3

        rule.setContent {
            TeacherNavigationBar(visible = true) {
                for (i in 0..<bars) {
                    TeacherNavigationBarItem(
                        selected = i == 0,
                        onClick = {},
                        icon = TeacherIcons.add().icon,
                        iconContentDescription = null,
                        label = "Label",
                    )
                }
            }
        }

        rule.onRoot(useUnmergedTree = true)
            .onChild()
            .apply { printToLog("NavigationBar") }
            .onChild()
            .onChildren()
            .assertCountEquals(bars)
            .assertAll(isSelectable() and hasClickAction())
    }
}