package com.example.teacherapp.core.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.ui.component.tab.TeacherTabRow
import com.example.teacherapp.core.ui.model.ActionItem
import com.example.teacherapp.core.ui.theme.TeacherAppTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TeacherTopBarWithTabs(
    title: String,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (studentTabIndex: Int) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    isTopBarVisible: Boolean = true,
    menuItems: List<ActionItem> = emptyList(),
    content: @Composable (tabIndex: Int) -> Unit,
) {
    if (!isTopBarVisible) {
        Box(modifier = modifier) {
            content(selectedTabIndex)
        }
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TeacherTopBar(
                    title = title,
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = onNavigationIconClick,
                    visible = true,
                    menuItems = menuItems,
                    scrollBehavior = scrollBehavior,
                )

                TeacherTabRow(
                    tabs = tabs,
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
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                content(page)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TeacherTopBarWithTabsPreview() {
    TeacherAppTheme {
        Surface {
            var selectedTabIndex by remember { mutableStateOf(0) }

            TeacherTopBarWithTabs(
                title = "Title",
                showNavigationIcon = true,
                onNavigationIconClick = {},
                tabs = listOf("Dane", "Oceny", "Uwagi"),
                selectedTabIndex = selectedTabIndex,
                onTabClick = { selectedTabIndex = it },
                pagerState = rememberPagerState(),
                isTopBarVisible = true,
                menuItems = listOf(),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            ) { tabIndex ->
                Box(Modifier.fillMaxSize()) {
                    Text(text = "Page index $tabIndex")
                }
            }
        }
    }
}