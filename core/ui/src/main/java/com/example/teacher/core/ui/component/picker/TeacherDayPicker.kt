package com.example.teacher.core.ui.component.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.component.TeacherChip
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.component.TeacherTextButton
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import java.time.DayOfWeek

@Composable
fun TeacherDayPicker(
    day: DayOfWeek,
    onDaySelected: (DayOfWeek) -> Unit,
    label: String,
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
        trailingIcon = {
            val icon = TeacherIcons.dayPick()
            Icon(imageVector = icon.icon, contentDescription = null)
        },
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
                        label = TimeUtils.getDisplayNameOfDayOfWeek(day).capitalize(Locale.current),
                        selected = selectedDay == day,
                        onClick = { onDaySelected(day) },
                    )
                }
            }
        },
        confirmButton = {
            TeacherTextButton(label = stringResource(R.string.ui_ok), onClick = onDismissRequest)
        },
    )
}

@Preview
@Composable
private fun TeacherDayPickerPreview() {
    TeacherTheme {
        Surface {
            var day by remember { mutableStateOf(TimeUtils.monday()) }

            TeacherDayPicker(
                day = day,
                onDaySelected = { day = it },
                label = "Pick day",
            )
        }
    }
}