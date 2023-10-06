package com.example.teacher.core.ui.util

import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.component.TeacherDiscardDialog

@Composable
fun BackPressDiscardDialogHandler(
    backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    title: String = stringResource(R.string.ui_discard_title),
    text: String = stringResource(R.string.ui_discard_message),
    enabled: Boolean,
    onDiscard: () -> Unit,
) {
    if (!enabled) {
        return
    }

    var showDialog by rememberSaveable { mutableStateOf(false) }

    BackPressHandler(backPressedDispatcher = backPressedDispatcher) {
        showDialog = true
    }

    if (showDialog) {
        TeacherDiscardDialog(
            title = title,
            text = text,
            onDismissRequest = { showDialog = false },
            onConfirmClick = {
                showDialog = false
                onDiscard()
            },
        )
    }
}