package com.example.teacher.core.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import kotlin.math.roundToInt

@Composable
fun TeacherIntSlider(
    label: String,
    min: Int,
    max: Int,
    value: Int,
    onValueChange: (value: Int) -> Unit,
    modifier: Modifier = Modifier,
    icon: TeacherIcon? = null,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(imageVector = icon.icon, contentDescription = null)
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
        }

        Column {
            Text(
                modifier = Modifier.padding(start = MaterialTheme.spacing.small),
                text = label,
                style = MaterialTheme.typography.labelLarge,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                val steps = 1
                Slider(
                    modifier = Modifier.weight(1f),
                    value = value.toFloat(),
                    onValueChange = { newValue -> onValueChange(newValue.roundToInt()) },
                    valueRange = min.toFloat()..max.toFloat(),
                    steps = (max - min) / steps - 1,
                )

                Text(text = "$value%")
            }
        }
    }
}

@Preview
@Composable
private fun TeacherIntSliderPreview() {
    TeacherTheme {
        Surface {
            var value by remember { mutableIntStateOf(50) }

            TeacherIntSlider(
                label = "Label",
                min = 0,
                max = 100,
                value = 33,
                onValueChange = { value = it }
            )
        }
    }
}