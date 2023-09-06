package com.example.teacher.feature.note.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.teacher.core.model.data.NotePriority
import com.example.teacher.feature.note.R

@Composable
internal fun priorityToName(notePriority: NotePriority): String {
    return when (notePriority) {
        NotePriority.Low -> stringResource(R.string.note_priority_low)
        NotePriority.Medium -> stringResource(R.string.note_priority_medium)
        NotePriority.High -> stringResource(R.string.note_priority_high)
    }
}