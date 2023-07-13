package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.Student
import com.example.teacherapp.ui.components.resource.ResourceContent
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab
import com.example.teacherapp.ui.screens.student.components.StudentScaffold
import com.example.teacherapp.ui.screens.student.data.StudentScaffoldViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun StudentScaffoldWrapper(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    menuItems: List<ActionMenuItem>,
    modifier: Modifier = Modifier,
    viewModel: StudentScaffoldViewModel = hiltViewModel(),
    content: @Composable (selectedTab: StudentTab, student: Student) -> Unit,
) {
    val studentResource by viewModel.studentResource.collectAsStateWithLifecycle()
    val isStudentDeleted by viewModel.isStudentDeleted.collectAsStateWithLifecycle()

    val tabs = remember { listOf(StudentTab.Grades, StudentTab.Detail, StudentTab.Notes) }
    val pagerState = rememberPagerState(initialPage = tabs.indexOf(StudentTab.Detail))
    val selectedTab by remember {
        derivedStateOf {
            tabs[pagerState.currentPage]
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // Observe deletion.
    LaunchedEffect(isStudentDeleted) {
        if (isStudentDeleted) {
            onShowSnackbar("Usunięto ucznia")
            onNavBack()
        }
    }

    val schoolClassName = remember(studentResource) {
        (studentResource as? Resource.Success)?.data?.schoolClass?.name.orEmpty()
    }

    StudentScaffold(
        isScaffoldVisible = !isStudentDeleted,
        title = "Klasa $schoolClassName",
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
        ResourceContent(
            modifier = modifier,
            resource = studentResource,
            isDeleted = isStudentDeleted,
            deletedMessage = "Usunięto pomyślnie dane ucznia."
        ) { student ->
            content(selectedTab = pagerTab, student = student)
        }
    }
}