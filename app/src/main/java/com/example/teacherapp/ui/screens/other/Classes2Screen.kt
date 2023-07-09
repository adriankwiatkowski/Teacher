package com.example.teacherapp.ui.screens.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun Classes2Screen(
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(30) {
            ClassItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                subject = "Fizyka",
                className = "2A",
                classSize = 24,
                isAdvanced = it % 2 == 0,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ClassItem(
    subject: String,
    className: String,
    classSize: Int,
    isAdvanced: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(elevation = 8.dp, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            FlowRow(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val textPadding = 4.dp

                Text(
                    modifier = Modifier.padding(horizontal = textPadding),
                    text = subject,
                    style = MaterialTheme.typography.h5,
                )
                if (isAdvanced) {
                    Text(
                        modifier = Modifier.padding(end = textPadding),
                        text = "(Rozszerzony)",
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
                Text(
                    modifier = Modifier.padding(end = textPadding),
                    text = className,
                    style = MaterialTheme.typography.subtitle1,
                )
                Row {
                    Text(
                        modifier = Modifier.padding(end = textPadding),
                        text = "($classSize)",
                        style = MaterialTheme.typography.subtitle1,
                    )
                    Image(
                        Icons.Filled.Person,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Classes2ScreenPreview() {
    TeacherAppTheme {
        Surface {
            Classes2Screen()
        }
    }
}