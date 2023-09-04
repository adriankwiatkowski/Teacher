package com.example.teacher.feature.schedule.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.ui.component.TeacherRadioButton
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
internal fun EventTypeControls(
    type: EventType,
    onTypeChange: (type: EventType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.selectableGroup()) {
        TeacherRadioButton(
            label = "Jednorazowe",
            selected = type == EventType.Once,
            onClick = { onTypeChange(EventType.Once) },
        )
        TeacherRadioButton(
            label = "Cotygodniowe",
            selected = type == EventType.Weekly,
            onClick = { onTypeChange(EventType.Weekly) },
        )
        TeacherRadioButton(
            label = "Co 2 tygodnie",
            selected = type == EventType.EveryTwoWeeks,
            onClick = { onTypeChange(EventType.EveryTwoWeeks) },
        )
    }
}

@Preview
@Composable
private fun EventTypeControlsPreview() {
    TeacherTheme {
        Surface {
            var type by remember { mutableStateOf(EventType.Weekly) }

            EventTypeControls(
                type = type,
                onTypeChange = { type = it },
            )
        }
    }
}