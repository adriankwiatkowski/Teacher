package com.example.teacherapp.ui.components.resource

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DeletedScreen(
    modifier: Modifier = Modifier,
    label: String = "Usunięto dane",
) {
    DeletedScreen(
        modifier = modifier,
        label = { Text(text = label, style = MaterialTheme.typography.displayMedium) },
    )
}

@Composable
fun DeletedScreen(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        label()
    }
}