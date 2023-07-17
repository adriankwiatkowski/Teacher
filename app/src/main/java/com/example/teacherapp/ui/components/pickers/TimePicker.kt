package com.example.teacherapp.ui.components.pickers

import android.app.TimePickerDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.components.form.TeacherChip
import com.example.teacherapp.ui.theme.TeacherAppTheme
import java.time.LocalTime

// TODO: Migrate to M3 Time Picker.
@Composable
fun TimePicker(
    time: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val dialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            onTimeSelected(LocalTime.of(hour, minute))
        },
        time.hour,
        time.minute,
        true,
    )

    TeacherChip(
        modifier = modifier,
        onClick = { dialog.show() },
        label = label,
    )
}

@Preview(showBackground = true)
@Composable
private fun TimePickerPreview() {
    TeacherAppTheme {
        Surface {
            TimePicker(
                time = LocalTime.now(),
                onTimeSelected = {},
                label = { Text("Pick time") },
            )
        }
    }
}