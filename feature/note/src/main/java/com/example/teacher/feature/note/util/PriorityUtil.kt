package com.example.teacher.feature.note.util

import androidx.compose.runtime.Composable
import com.example.teacher.core.model.data.NotePriority

@Composable
internal fun priorityToName(notePriority: NotePriority): String {
    return when (notePriority) {
        NotePriority.Low -> "Nieważne"
        NotePriority.Medium -> "Ważne"
        NotePriority.High -> "Bardzo ważne"
    }
}