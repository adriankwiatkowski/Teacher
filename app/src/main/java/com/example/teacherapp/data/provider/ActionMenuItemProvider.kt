package com.example.teacherapp.data.provider

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import com.example.teacherapp.data.models.ActionMenuItem

object ActionMenuItemProvider {

    fun edit(onClick: () -> Unit) = ActionMenuItem(
        name = "",
        imageVector = Icons.Default.Edit,
        contentDescription = null,
        onClick = onClick,
    )

    fun delete(onClick: () -> Unit) = ActionMenuItem(
        name = "",
        imageVector = Icons.Default.Delete,
        contentDescription = null,
        onClick = onClick,
    )
}