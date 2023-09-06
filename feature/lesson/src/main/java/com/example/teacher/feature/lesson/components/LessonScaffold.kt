package com.example.teacher.feature.lesson.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.component.TeacherTopBarDefaults
import com.example.teacher.core.ui.component.TeacherTopBarWithTabs
import com.example.teacher.core.ui.model.TeacherAction
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.feature.lesson.tab.LessonTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun LessonScaffold(
    isScaffoldVisible: Boolean,
    title: String,
    menuItems: List<TeacherAction>,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    tabs: List<LessonTab>,
    selectedTab: LessonTab,
    onTabClick: (studentTab: LessonTab) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (lessonTab: LessonTab) -> Unit,
) {
    val tabIcons = remember(tabs) { tabs.map(LessonTab::icon) }
    val selectedTabIndex = remember(tabs, selectedTab) { tabs.indexOf(selectedTab) }

    val scrollBehavior = TeacherTopBarDefaults.default()

    TeacherTopBarWithTabs(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        title = title,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavigationIconClick,
        tabs = tabIcons,
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
    TeacherTheme {
        Surface {
            val tabs = remember {
                listOf(
                    LessonTab.Grades,
                    LessonTab.Attendance,
                    LessonTab.Activity,
                    LessonTab.Notes,
                )
            }
            val pagerState =
                rememberPagerState(initialPage = tabs.indexOf(LessonTab.Grades)) { tabs.size }
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
                    Text(stringResource(tab.icon.text))
                    repeat(10) {
                        Text("Details of tab...")
                    }
                }
            }
        }
    }
}