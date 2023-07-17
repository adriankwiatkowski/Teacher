package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.ui.components.TeacherTopBarDefaults
import com.example.teacherapp.ui.components.TeacherTopBarWithTabs
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab
import com.example.teacherapp.ui.theme.TeacherAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StudentScaffold(
    isScaffoldVisible: Boolean,
    title: String,
    menuItems: List<ActionMenuItem>,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    tabs: List<StudentTab>,
    selectedTab: StudentTab,
    onTabClick: (studentTab: StudentTab) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (studentTab: StudentTab) -> Unit,
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
private fun StudentScaffoldPreview() {
    TeacherAppTheme {
        Surface {
            val tabs = remember { listOf(StudentTab.Grades, StudentTab.Detail, StudentTab.Notes) }
            val pagerState = rememberPagerState(initialPage = tabs.indexOf(StudentTab.Detail))
            val selectedTab = tabs[pagerState.currentPage]

            val coroutineScope = rememberCoroutineScope()

            StudentScaffold(
                modifier = Modifier.fillMaxSize(),
                isScaffoldVisible = true,
                title = "Klasa 3A",
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
            ) { studentTab ->
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(studentTab.title)
                    repeat(10) {
                        Text("Details of tab...")
                    }
                }
            }
        }
    }
}