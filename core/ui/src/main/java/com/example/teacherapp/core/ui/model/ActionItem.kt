package com.example.teacherapp.core.ui.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class ActionItem(
    val name: String,
    val imageVector: ImageVector,
    val contentDescription: String? = null,
    val onClick: () -> Unit,
)