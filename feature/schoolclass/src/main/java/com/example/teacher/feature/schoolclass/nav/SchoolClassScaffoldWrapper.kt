package com.example.teacher.feature.schoolclass.nav

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.model.TeacherAction
import com.example.teacher.core.ui.util.OnShowSnackbar
import com.example.teacher.feature.schoolclass.R
import com.example.teacher.feature.schoolclass.component.SchoolClassScaffold
import com.example.teacher.feature.schoolclass.data.SchoolClassViewModel
import com.example.teacher.feature.schoolclass.tab.SchoolClassTab
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SchoolClassScaffoldWrapper(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: OnShowSnackbar,
    menuItems: List<TeacherAction>,
    modifier: Modifier = Modifier,
    viewModel: SchoolClassViewModel = hiltViewModel(),
    content: @Composable (selectedTab: SchoolClassTab, schoolClass: SchoolClass) -> Unit,
) {
    val schoolClassResult by viewModel.schoolClassResult.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()

    val tabs = remember {
        listOf(
            SchoolClassTab.Students,
            SchoolClassTab.Subjects,
            SchoolClassTab.Detail,
        )
    }
    val pagerState =
        rememberPagerState(initialPage = tabs.indexOf(SchoolClassTab.Students)) { tabs.size }
    val selectedTab by remember {
        derivedStateOf {
            tabs[pagerState.currentPage]
        }
    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Observe deletion.
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onShowSnackbar.onShowSnackbar(R.string.school_class_school_class_deleted)
            onNavBack()
        }
    }

    val title = remember(schoolClassResult) {
        val result = (schoolClassResult as? Result.Success)
            ?: return@remember context.getString(R.string.school_class_label)
        val schoolClass = result.data
        context.getString(R.string.school_class, schoolClass.name)
    }

    SchoolClassScaffold(
        modifier = modifier,
        isScaffoldVisible = !isDeleted,
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
            result = schoolClassResult,
            isDeleted = isDeleted,
            deletedMessage = stringResource(R.string.school_class_school_class_deleted),
        ) { schoolClass ->
            content(pagerTab, schoolClass)
        }
    }
}