package com.example.teacherapp.ui.components.resource

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.teacherapp.core.common.result.Result

@Composable
fun <T> ResultContent(
    result: Result<T>,
    modifier: Modifier = Modifier,
    isDeleted: Boolean = false,
    deletedMessage: String = "Usunięto dane",
    loadingContent: @Composable (() -> Unit)? = null,
    errorContent: @Composable (() -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    Box(modifier = modifier) {
        if (isDeleted) {
            DeletedScreen(label = deletedMessage)
            return
        }

        when (result) {
            Result.Loading -> if (loadingContent != null) loadingContent() else LoadingScreen()
            is Result.Error -> if (errorContent != null) errorContent() else ErrorScreen()
            is Result.Success -> content(result.data)
        }
    }
}