package com.example.teacherapp.ui.components.resource

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.teacherapp.data.models.Resource

@Composable
fun <T> ResourceContent(
    resource: Resource<T>,
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
            Resource.Loading -> LoadingScreen()
            is Resource.Error -> ErrorScreen()
            is Resource.Success -> content(resource.data)
        }
    }
}