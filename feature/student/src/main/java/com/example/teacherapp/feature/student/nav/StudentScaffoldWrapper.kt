package com.example.teacherapp.feature.student.nav

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
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.core.ui.component.result.ResultContent
import com.example.teacherapp.core.ui.model.ActionItem
import com.example.teacherapp.feature.student.components.StudentScaffold
import com.example.teacherapp.feature.student.data.StudentScaffoldViewModel
import com.example.teacherapp.feature.student.tab.StudentTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun StudentScaffoldWrapper(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    menuItems: List<ActionItem>,
    modifier: Modifier = Modifier,
    viewModel: StudentScaffoldViewModel = hiltViewModel(),
    content: @Composable (selectedTab: StudentTab, student: Student) -> Unit,
) {
    val studentResult by viewModel.studentResult.collectAsStateWithLifecycle()
    val isStudentDeleted by viewModel.isStudentDeleted.collectAsStateWithLifecycle()

    val tabs = remember { listOf(StudentTab.Detail, StudentTab.Grades, StudentTab.Notes) }
    val pagerState = rememberPagerState(initialPage = tabs.indexOf(StudentTab.Detail))
    val selectedTab by remember {
        derivedStateOf {
            tabs[pagerState.currentPage]
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // Observe deletion.
    LaunchedEffect(isStudentDeleted, onShowSnackbar, onNavBack) {
        if (isStudentDeleted) {
            onShowSnackbar("Usunięto ucznia")
            onNavBack()
        }
    }

    val title = remember(studentResult) {
        val result = (studentResult as? Result.Success) ?: return@remember "Uczeń"
        val data = result.data
        "${data.fullName} ${data.schoolClass.name}"
    }

    StudentScaffold(
        modifier = modifier,
        isScaffoldVisible = !isStudentDeleted,
        title = title,
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
            result = studentResult,
            isDeleted = isStudentDeleted,
            deletedMessage = "Usunięto pomyślnie dane ucznia."
        ) { student ->
            content(selectedTab = pagerTab, student = student)
        }
    }
}