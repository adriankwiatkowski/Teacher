package com.example.teacher.feature.schedule.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.feature.schedule.R

@Composable
internal fun LessonTermPicker(
    firstTermName: String,
    secondTermName: String,
    isFirstTermSelected: Boolean,
    onTermSelected: (isFirstTermSelected: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.schedule_term),
            style = MaterialTheme.typography.labelLarge,
        )

        Column(Modifier.selectableGroup()) {
            TeacherRadioButton(
                label = stringResource(R.string.schedule_term_with_data, firstTermName),
                selected = isFirstTermSelected,
                onClick = { onTermSelected(true) },
            )
            TeacherRadioButton(
                label = stringResource(R.string.schedule_term_with_data, secondTermName),
                selected = !isFirstTermSelected,
                onClick = { onTermSelected(false) },
            )
        }
    }
}

@Preview
@Composable
private fun LessonTermPickerPreview() {
    TeacherTheme {
        Surface {
            var isFirstTermSelected by remember { mutableStateOf(true) }

            LessonTermPicker(
                firstTermName = "I",
                secondTermName = "II",
                isFirstTermSelected = isFirstTermSelected,
                onTermSelected = { isFirstTermSelected = it },
            )
        }
    }
}