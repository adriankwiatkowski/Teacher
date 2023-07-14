package com.example.teacherapp.ui.components.resource

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.teacherapp.core.common.result.Result

@Composable
fun <T> ResourceContent(
    resource: Result<T>,
    modifier: Modifier = Modifier,
    isDeleted: Boolean = false,
    deletedMessage: String = "Usunięto dane",
    content: @Composable (T) -> Unit,
) {
    Box(modifier = modifier) {
        if (isDeleted) {
            DeletedScreen(label = deletedMessage)
            return
        }

        when (resource) {
            Result.Loading -> LoadingScreen()
            is Result.Error -> ErrorScreen()
            is Result.Success -> content(resource.data)
        }
    }
}