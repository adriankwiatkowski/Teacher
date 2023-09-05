package com.example.teacher.core.ui.component

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.component.tab.TeacherTabRow
import com.example.teacher.core.ui.model.TeacherAction
import com.example.teacher.core.ui.model.StringResource
import com.example.teacher.core.ui.theme.TeacherTheme

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TeacherTopBarWithTabs(
    title: String,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
    tabs: List<StringResource>,
    selectedTabIndex: Int,
    onTabClick: (studentTabIndex: Int) -> Unit,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    isTopBarVisible: Boolean = true,
    menuItems: List<TeacherAction> = emptyList(),
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
        HorizontalPager(state = pagerState) { page ->
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
    TeacherTheme {
        Surface {
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val tabs = remember {
                listOf(
                    StringResource(R.string.tab_1),
                    StringResource(R.string.tab_2),
                    StringResource(R.string.tab_3),
                )
            }

            TeacherTopBarWithTabs(
                title = "Title",
                showNavigationIcon = true,
                onNavigationIconClick = {},
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabClick = { selectedTabIndex = it },
                pagerState = rememberPagerState { tabs.size },
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