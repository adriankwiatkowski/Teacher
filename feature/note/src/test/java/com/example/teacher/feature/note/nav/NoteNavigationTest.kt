package com.example.teacher.feature.note.nav

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.feature.note.R
import com.example.teacher.feature.note.nav.NoteNavigation.notesRoute
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NoteNavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console

        val snackbarHostState = SnackbarHostState()
        rule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavHost(navController = navController, startDestination = notesRoute) {
                noteGraph(
                    navController = navController,
                    snackbarHostState = snackbarHostState,
                    onShowSnackbar = {},
                )
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        assertEquals(notesRoute, navController.currentBackStackEntry!!.destination.route)
    }

    @Test
    fun navHost_verifyStartDestinationDisplayed() {
        val fabContentDescription = rule.activity.getString(TeacherIcons.add().text)
        rule.onNodeWithContentDescription(fabContentDescription).assertIsDisplayed()
    }

    @Test
    fun navHost_notesToNote_happyPath() {
        val fabContentDescription = rule.activity.getString(TeacherIcons.add().text)
        rule.onNodeWithContentDescription(fabContentDescription).performClick()

        assertEquals(noteFormRoute, navController.currentBackStackEntry!!.destination.route)
    }

    @Test
    fun navHost_notesToNote_fromDisplayed() {
        val fabContentDescription = rule.activity.getString(TeacherIcons.add().text)
        rule.onNodeWithContentDescription(fabContentDescription).performClick()

        val noteFormTitle = rule.activity.getString(R.string.note_note_form_title)
        rule.onNodeWithText(noteFormTitle).assertIsDisplayed()
    }
}