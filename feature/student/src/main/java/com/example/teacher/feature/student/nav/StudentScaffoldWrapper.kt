package com.example.teacher.feature.student.nav

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.TeacherAction
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.student.R
import com.example.teacher.feature.student.component.StudentScaffold
import com.example.teacher.feature.student.data.StudentScaffoldViewModel
import com.example.teacher.feature.student.tab.StudentTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun StudentScaffoldWrapper(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: OnShowSnackbar,
    menuItems: List<TeacherAction>,
    modifier: Modifier = Modifier,
    viewModel: StudentScaffoldViewModel = hiltViewModel(),
    content: @Composable (selectedTab: StudentTab, student: Student) -> Unit,
) {
    val studentResult by viewModel.studentResult.collectAsStateWithLifecycle()
    val isStudentDeleted by viewModel.isStudentDeleted.collectAsStateWithLifecycle()

    val tabs = remember { listOf(StudentTab.Grades, StudentTab.Notes, StudentTab.Detail) }
    val pagerState = rememberPagerState(initialPage = tabs.indexOf(StudentTab.Grades)) { tabs.size }
    val selectedTab by remember {
        derivedStateOf {
            tabs[pagerState.currentPage]
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // Observe deletion.
    LaunchedEffect(isStudentDeleted) {
        if (isStudentDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.student_deleted)
            onNavBack()
        }
    }

    val defaultTitle = stringResource(R.string.student_student)
    val title = remember(studentResult, defaultTitle) {
        val result = (studentResult as? Result.Success) ?: return@remember defaultTitle
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
            deletedMessage = stringResource(R.string.student_deleted),
        ) { student ->
            content(pagerTab, student)
        }
    }
}