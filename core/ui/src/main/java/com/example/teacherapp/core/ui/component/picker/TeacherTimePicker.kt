package com.example.teacherapp.core.ui.component.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.teacherapp.core.ui.component.TeacherChip
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import java.time.LocalTime

// TODO: Migrate to M3 Time Picker. Replace TimePickerDialog with appropriate component when it will become available.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherTimePicker(
    time: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        val timePickerState = rememberTimePickerState(
            initialHour = time.hour,
            initialMinute = time.minute,
        )

        TimePickerDialog(
            onCancel = { showDialog = false },
            onConfirm = {
                val hour = timePickerState.hour
                val minute = timePickerState.minute
                onTimeSelected(LocalTime.of(hour, minute))
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }

    TeacherChip(
        modifier = modifier,
        onClick = { showDialog = true },
        label = label,
    )
}

@Composable
private fun TimePickerDialog(
    title: String = "Wybierz czas",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) {
                        Text("Anuluj")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TeacherTimePickerPreview() {
    TeacherAppTheme {
        Surface {
            var time = remember { LocalTime.now() }

            TeacherTimePicker(
                time = time,
                onTimeSelected = { time = it },
                label = { Text("Pick time") },
            )
        }
    }
}