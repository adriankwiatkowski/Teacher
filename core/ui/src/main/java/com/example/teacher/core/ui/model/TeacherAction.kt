package com.example.teacher.core.ui.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class TeacherAction(
    @StringRes val text: Int,
    val imageVector: ImageVector,
    @StringRes val contentDescription: Int? = null,
    val onClick: () -> Unit,
)