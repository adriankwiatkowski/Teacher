package com.example.teacher.feature.note.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.model.data.NotePriority
import com.example.teacher.core.ui.component.TeacherInputChip
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.note.R
import com.example.teacher.feature.note.util.priorityToName

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun NotePriorityPicker(
    selectedPriority: NotePriority,
    onPriorityChange: (priority: NotePriority) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
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
                    TeacherInputChip(
                        selected = priority == selectedPriority,
                        onClick = { onPriorityChange(priority) },
                        label = priorityToName(priority)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotePriorityPickerPreview() {
    TeacherTheme {
        Surface {
            var priority by remember { mutableStateOf(NotePriority.Medium) }

            NotePriorityPicker(
                selectedPriority = priority,
                onPriorityChange = { priority = it },
            )
        }
    }
}