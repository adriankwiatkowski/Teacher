package com.example.teacherapp.ui.nav.graphs.lesson.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.teacherapp.ui.screens.grade.GradesScreen
import com.example.teacherapp.ui.screens.grade.data.GradesViewModel

@Composable
internal fun GradesRoute(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onShowSnackbar: (message: String) -> Unit,
    onEditClick: () -> Unit,
    viewModel: GradesViewModel = hiltViewModel(),
) {
    val uiStateResult by viewModel.uiState.collectAsStateWithLifecycle()
    val isDeleted by viewModel.isDeleted.collectAsStateWithLifecycle()

    // Observe deletion.
    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            onShowSnackbar("Usunięto ocenę")
            onNavBack()
        }
    }

    GradesScreen(
        uiStateResult = uiStateResult,
        showNavigationIcon = showNavigationIcon,
        onNavBack = onNavBack,
        isDeleted = isDeleted,
        onDeleteClick = viewModel::onDelete,
        onEditClick = onEditClick,
    )
}