package com.example.teacher.feature.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.teacher.core.model.data.NotePriority

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun NotePriorityPicker(
    priority: NotePriority,
    onPriorityChange: (priority: NotePriority) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Ważność notatki", style = MaterialTheme.typography.bodyLarge)

            FlowRow(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                InputChip(
                    selected = priority == NotePriority.Low,
                    onClick = { onPriorityChange(NotePriority.Low) },
                    label = { Text("Nieważne") }
                )
                InputChip(
                    selected = priority == NotePriority.Medium,
                    onClick = { onPriorityChange(NotePriority.Medium) },
                    label = { Text("Ważne") }
                )
                InputChip(
                    selected = priority == NotePriority.High,
                    onClick = { onPriorityChange(NotePriority.High) },
                    label = { Text("Bardzo ważne") }
                )
            }
        }
    }
}