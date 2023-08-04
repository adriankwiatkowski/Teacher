package com.example.teacherapp.feature.lesson.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.ui.component.TeacherTopBarDefaults
import com.example.teacherapp.core.ui.component.TeacherTopBarWithTabs
import com.example.teacherapp.core.ui.model.ActionItem
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.feature.lesson.tab.LessonTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun LessonScaffold(
    isScaffoldVisible: Boolean,
    title: String,
    menuItems: List<ActionItem>,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    tabs: List<LessonTab>,
    selectedTab: LessonTab,
    onTabClick: (studentTab: LessonTab) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (lessonTab: LessonTab) -> Unit,
) {
    val stringTabs = remember(tabs) {
        tabs.map { tab -> tab.title }
    }
    val selectedTabIndex = remember(tabs, selectedTab) {
        tabs.indexOf(selectedTab)
    }

    val scrollBehavior = TeacherTopBarDefaults.default()

    TeacherTopBarWithTabs(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = title,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
        tabs = stringTabs,
        selectedTabIndex = selectedTabIndex,
        onTabClick = { index -> onTabClick(tabs[index]) },
        pagerState = pagerState,
        isTopBarVisible = isScaffoldVisible,
        menuItems = menuItems,
        scrollBehavior = scrollBehavior,
    ) { tabIndex ->
        content(tabs[tabIndex])
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun LessonScaffoldPreview() {
    TeacherAppTheme {
        Surface {
            val tabs = remember {
                listOf(
                    LessonTab.Grades,
                    LessonTab.Attendance,
                    LessonTab.Activity,
                    LessonTab.Notes,
                )
            }
            val pagerState = rememberPagerState(initialPage = tabs.indexOf(LessonTab.Grades))
            val selectedTab = tabs[pagerState.currentPage]

            val coroutineScope = rememberCoroutineScope()

            LessonScaffold(
                modifier = Modifier.fillMaxSize(),
                isScaffoldVisible = true,
                title = "Matematyka 3A",
                menuItems = listOf(),
                showNavigationIcon = true,
                onNavigationIconClick = {},
                tabs = tabs,
                selectedTab = selectedTab,
                onTabClick = { tab ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(tabs.indexOf(tab))
                    }
                },
                pagerState = pagerState,
            ) { tab ->
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(tab.title)
                    repeat(10) {
                        Text("Details of tab...")
                    }
                }
            }
        }
    }
}