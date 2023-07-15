package com.example.teacherapp.ui.nav.graphs.lesson.route

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.ui.components.resource.ResultContent
import com.example.teacherapp.ui.nav.graphs.lesson.tab.LessonTab
import com.example.teacherapp.ui.screens.lesson.components.LessonScaffold
import com.example.teacherapp.ui.screens.lesson.data.LessonScaffoldViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LessonScaffoldWrapper(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    menuItems: List<ActionMenuItem>,
    modifier: Modifier = Modifier,
    viewModel: LessonScaffoldViewModel = hiltViewModel(),
    content: @Composable (selectedTab: LessonTab, lesson: Lesson) -> Unit,
) {
    val lessonResult by viewModel.studentResult.collectAsStateWithLifecycle()
    val isLessonDeleted by viewModel.isLessonDeleted.collectAsStateWithLifecycle()

    val tabs = remember { listOf(LessonTab.Grades, LessonTab.Activity) }
    val pagerState = rememberPagerState(initialPage = tabs.indexOf(LessonTab.Grades))
    val selectedTab by remember {
        derivedStateOf {
            tabs[pagerState.currentPage]
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // Observe deletion.
    LaunchedEffect(isLessonDeleted) {
        if (isLessonDeleted) {
            onShowSnackbar("Usunięto zajęcia")
            onNavBack()
        }
    }

    val lessonName = remember(lessonResult) {
        (lessonResult as? Result.Success)?.data?.name.orEmpty()
    }
    val schoolClassName = remember(lessonResult) {
        (lessonResult as? Result.Success)?.data?.schoolClass?.name.orEmpty()
    }

    LessonScaffold(
        modifier = modifier,
        isScaffoldVisible = !isLessonDeleted,
        title = "$lessonName $schoolClassName",
        menuItems = menuItems,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavBack,
        tabs = tabs,
        selectedTab = selectedTab,
        onTabClick = { tab ->
            coroutineScope.launch {
                pagerState.animateScrollToPage(tabs.indexOf(tab))
            }
        },
        pagerState = pagerState,
    ) { pagerTab ->
        ResultContent(
            result = lessonResult,
            isDeleted = isLessonDeleted,
            deletedMessage = "Usunięto zajęcia."
        ) { lesson ->
            content(selectedTab = pagerTab, lesson = lesson)
        }
    }
}