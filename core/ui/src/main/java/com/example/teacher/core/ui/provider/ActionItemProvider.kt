package com.example.teacher.core.ui.provider

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import com.example.teacher.core.ui.model.ActionItem

object ActionItemProvider {

    fun edit(onClick: () -> Unit) = ActionItem(
        name = "",
        imageVector = Icons.Default.Edit,
        contentDescription = null,
        onClick = onClick,
    )

    fun delete(onClick: () -> Unit) = ActionItem(
        name = "",
        imageVector = Icons.Default.Delete,
        contentDescription = null,
        onClick = onClick,
    )
}