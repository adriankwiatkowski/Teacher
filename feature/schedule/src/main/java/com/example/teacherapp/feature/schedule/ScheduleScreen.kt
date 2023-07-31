package com.example.teacherapp.feature.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.core.ui.component.TeacherButton
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing

@Composable
internal fun ScheduleScreen(
    onAddScheduleClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.small),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Plan zajęć", style = MaterialTheme.typography.headlineMedium)

        TeacherButton(onClick = onAddScheduleClick) {
            Text(text = "Dodaj termin zajęć")
        }
    }
}

@Preview
@Composable
private fun ScheduleScreenPreview() {
    TeacherAppTheme {
        Surface {
            ScheduleScreen(
                onAddScheduleClick = {},
            )
        }
    }
}