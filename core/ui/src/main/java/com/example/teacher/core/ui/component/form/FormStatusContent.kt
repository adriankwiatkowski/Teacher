package com.example.teacher.core.ui.component.form

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.teacher.core.ui.component.result.ErrorScreen
import com.example.teacher.core.ui.component.result.LoadingScreen
import com.example.teacher.core.ui.model.FormStatus

@Composable
fun FormStatusContent(
    formStatus: FormStatus,
    modifier: Modifier = Modifier,
    savingText: String = "Zapisywanie...",
    errorText: String = "Wystąpił nieoczekiwany błąd podczas zapisywania",
    successContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        when (formStatus) {
            FormStatus.Idle -> content()
            FormStatus.Saving -> LoadingScreen(label = savingText)
            FormStatus.Success -> if (successContent != null) successContent() else content()
            FormStatus.Error -> ErrorScreen(label = errorText)
        }
    }
}