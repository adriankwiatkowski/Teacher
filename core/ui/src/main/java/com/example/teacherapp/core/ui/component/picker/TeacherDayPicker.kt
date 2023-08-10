package com.example.teacherapp.core.ui.component.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.common.utils.TimeUtils
import com.example.teacherapp.core.ui.component.TeacherChip
import com.example.teacherapp.core.ui.component.TeacherRadioButton
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import java.time.DayOfWeek

@Composable
fun TeacherDayPicker(
    day: DayOfWeek,
    onDaySelected: (DayOfWeek) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        DayPickerDialog(
            selectedDay = day,
            onDaySelected = onDaySelected,
            onDismissRequest = { showDialog = false },
        )
    }

    TeacherChip(
        modifier = modifier,
        onClick = { showDialog = true },
        label = label,
    )
}

@Composable
private fun DayPickerDialog(
    selectedDay: DayOfWeek,
    onDaySelected: (DayOfWeek) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text("Wybierz dzień") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .selectableGroup(),
            ) {
                val days = remember { TimeUtils.days() }
                for (day in days) {
                    TeacherRadioButton(
                        label = TimeUtils.getDisplayNameOfDayOfWeek(day),
                        selected = selectedDay == day,
                        onClick = { onDaySelected(day) },
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Ok")
            }
        },
    )
}

@Preview
@Composable
private fun TeacherDayPickerPreview() {
    TeacherAppTheme {
        Surface {
            var day by remember { mutableStateOf(TimeUtils.monday()) }

            TeacherDayPicker(
                day = day,
                onDaySelected = { day = it },
                label = { Text("Pick day") },
            )
        }
    }
}