package com.example.teacherapp.data.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class FabAction(
    val imageVector: ImageVector,
    val contentDescription: String? = null,
    val onClick: () -> Unit,
)