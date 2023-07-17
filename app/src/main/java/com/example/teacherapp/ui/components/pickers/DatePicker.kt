package com.example.teacherapp.ui.components.pickers

import android.app.DatePickerDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.components.form.TeacherChip
import com.example.teacherapp.ui.theme.TeacherAppTheme
import java.time.LocalDate

// TODO: Migrate to M3 Date Picker.
@Composable
fun DatePicker(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val dialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month, dayOfMonth))
        },
        date.year,
        date.monthValue,
        date.dayOfMonth,
    )

//    var showDialog by rememberSaveable { mutableStateOf(false) }
//
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            confirmButton = {
//                Text("Zatwierdź")
//            },
//            dismissButton = {
//                Text("Anuluj")
//            },
//            title = {
//                Text("Wybierz datę")
//            },
//            text = {
//                AndroidView(
//                    factory = { context ->
//                        CalendarView(context)
//                    },
//                    update = { calendarView ->
//                        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
//                            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
//                        }
//                        calendarView.date = date
//                            .atStartOfDay(TimeZone.getDefault().toZoneId())
//                            .toInstant()
//                            .toEpochMilli()
//                    }
//                )
//            },
//        )
//    }

    TeacherChip(
        modifier = modifier,
        onClick = { dialog.show() },
        label = label,
    )
}

@Preview(showBackground = true)
@Composable
private fun DatePickerPreview() {
    TeacherAppTheme {
        Surface {
            DatePicker(
                date = LocalDate.now(),
                onDateSelected = {},
                label = { Text("Pick date") },
            )
        }
    }
}