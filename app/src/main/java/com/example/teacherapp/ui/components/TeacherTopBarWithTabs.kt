package com.example.teacherapp.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.ui.components.tab.TeacherTabRow
import com.example.teacherapp.ui.theme.TeacherAppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeacherTopBarWithTabs(
    title: String,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (studentTabIndex: Int) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    isTopBarVisible: Boolean = true,
    menuItems: List<ActionMenuItem> = emptyList(),
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
                content(tabIndex = page)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
            ) { tabIndex ->
                Box(Modifier.fillMaxSize()) {
                    Text(text = "Page index $tabIndex")
                }
            }
        }
    }
}