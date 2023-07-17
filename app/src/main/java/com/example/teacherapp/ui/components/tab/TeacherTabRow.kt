package com.example.teacherapp.ui.components.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun TeacherTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabClick: (studentTabIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                TeacherTab(
                    text = tab,
                    selected = index == selectedTabIndex,
                    onClick = { onTabClick(index) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun TeacherTabRowPreview() {
    TeacherAppTheme {
        Surface {
            var selectedTabIndex by remember { mutableStateOf(0) }

            TeacherTabRow(
                tabs = listOf("Dane", "Oceny", "Uwagi"),
                selectedTabIndex = selectedTabIndex,
                onTabClick = { selectedTabIndex = it },
            )
        }
    }
}