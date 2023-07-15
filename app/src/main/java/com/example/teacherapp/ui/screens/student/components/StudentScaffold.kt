package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab
import com.example.teacherapp.ui.theme.TeacherAppTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
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
    if (!isScaffoldVisible) {
        Box(modifier = modifier) {
            content(selectedTab)
        }
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TeacherTopBar(
                    title = title,
                    menuItems = menuItems,
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = onNavigationIconClick,
                    visible = true
                )

                StudentTabRow(
                    tabs = tabs,
                    selectedTab = selectedTab,
                    selectedTabIndex = pagerState.currentPage,
                    onTabClick = onTabClick,
                )
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            pageCount = tabs.size,
            state = pagerState,
        ) { page ->
            Column(modifier = Modifier.padding(innerPadding)) {
                content(studentTab = tabs[page])
            }
        }
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