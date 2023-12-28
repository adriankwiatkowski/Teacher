package com.example.teacher.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.ui.provider.TeacherActions
import com.example.teacher.core.ui.provider.TeacherIcons
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RunWith(AndroidJUnit4::class)
class TopBarWithTabsTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun titleExists() {
        rule.setContent {
            TeacherTopBarWithTabs(
                title = title,
                showNavigationIcon = true,
                onNavigationIconClick = {},
                scrollBehavior = null,
                tabs = listOf(TeacherIcons.details(), TeacherIcons.subject()),
                selectedTabIndex = 0,
                onTabClick = {},
                pagerState = rememberPagerState(pageCount = { 2 }),
                menuItems = listOf(TeacherActions.delete(onClick = {})),
            ) {}
        }

        rule.onNodeWithText(title).assertExists()
    }

    @Test
    fun pageContentExists() {
        rule.setContent {
            TeacherTopBarWithTabs(
                title = title,
                showNavigationIcon = true,
                onNavigationIconClick = {},
                scrollBehavior = null,
                tabs = listOf(TeacherIcons.details(), TeacherIcons.subject()),
                selectedTabIndex = 0,
                onTabClick = {},
                pagerState = rememberPagerState(pageCount = { 2 }),
                menuItems = listOf(TeacherActions.delete(onClick = {})),
            ) { page ->
                Text("Page $page")
            }
        }

        rule.onNodeWithText("Page 0").assertExists()
    }

    private val title = "Title"
}