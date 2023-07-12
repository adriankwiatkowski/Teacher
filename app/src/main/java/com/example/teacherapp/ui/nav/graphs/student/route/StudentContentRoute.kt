package com.example.teacherapp.ui.nav.graphs.student.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.Student
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.components.resource.ResourceContent
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab
import com.example.teacherapp.ui.screens.student.components.StudentContent
import com.example.teacherapp.ui.screens.student.data.StudentContentViewModel

@Composable
internal fun StudentContentRoute(
    selectedTab: StudentTab,
    onTabClick: (studentTab: StudentTab) -> Unit,
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    menuItems: List<ActionMenuItem>,
    modifier: Modifier = Modifier,
    viewModel: StudentContentViewModel = hiltViewModel(),
    content: @Composable (student: Student) -> Unit,
) {
    val studentResource by viewModel.studentResource.collectAsStateWithLifecycle()
    val isStudentDeleted = viewModel.isStudentDeleted

    // Observe deletion.
    LaunchedEffect(isStudentDeleted) {
        if (isStudentDeleted) {
            onShowSnackbar("Usunięto ucznia")
            onNavBack()
        }
    }

    val finalMenuItems = remember(menuItems, viewModel, viewModel::deleteStudent) {
        menuItems + ActionMenuItemProvider.delete(viewModel::deleteStudent)
    }

    val schoolClassName = remember(studentResource) {
        (studentResource as? Resource.Success)?.data?.schoolClass?.name.orEmpty()
    }

    StudentContent(
        title = "Klasa $schoolClassName",
        menuItems = finalMenuItems,
        showNavigationIcon = showNavigationIcon,
        onNavigationIconClick = onNavBack,
        selectedTab = selectedTab,
        onTabClick = onTabClick,
    ) {
        ResourceContent(
            modifier = modifier,
            resource = studentResource,
            isDeleted = isStudentDeleted,
            deletedMessage = "Usunięto pomyślnie dane ucznia."
        ) { student ->
            content(student)
        }
    }
}