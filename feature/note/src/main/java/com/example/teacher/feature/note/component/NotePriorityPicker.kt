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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.teacher.core.model.data.NotePriority
import com.example.teacher.feature.note.R
import com.example.teacher.feature.note.util.priorityToName

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun NotePriorityPicker(
    selectedPriority: NotePriority,
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
            Text(
                text = stringResource(R.string.note_note_priority),
                style = MaterialTheme.typography.bodyLarge,
            )

            FlowRow(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                val priorities =
                    remember { listOf(NotePriority.Low, NotePriority.Medium, NotePriority.High) }

                for (priority in priorities) {
                    InputChip(
                        selected = priority == selectedPriority,
                        onClick = { onPriorityChange(priority) },
                        label = { priorityToName(priority) }
                    )
                }
            }
        }
    }
}