package com.example.teacherapp.core.ui.component.picker

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.ui.component.TeacherChip
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDatePicker(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = TimeUtils.dateToMillis(date)
        )
        val confirmEnabled =
            remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        val epochMilli = datePickerState.selectedDateMillis
                        if (epochMilli != null) {
                            onDateSelected(TimeUtils.millisToDate(epochMilli))
                        }
                    },
                    enabled = confirmEnabled.value,
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Anuluj")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    TeacherChip(
        modifier = modifier,
        onClick = { showDialog = true },
        label = label,
    )
}

@Preview
@Composable
private fun TeacherDatePickerPreview() {
    TeacherAppTheme {
        Surface {
            var date by remember { mutableStateOf(TimeUtils.currentDate()) }

            TeacherDatePicker(
                date = date,
                onDateSelected = { date = it },
                label = { Text("Pick date") },
            )
        }
    }
}