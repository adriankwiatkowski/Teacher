package com.example.teacher.core.ui.component.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.model.StringResource
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun TeacherTabRow(
    tabs: List<StringResource>,
    selectedTabIndex: Int,
    onTabClick: (studentTabIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tab ->
                TeacherTab(
                    text = stringResource(tab.id),
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
    TeacherTheme {
        Surface {
            var selectedTabIndex by remember { mutableIntStateOf(0) }

            TeacherTabRow(
                tabs = listOf(
                    StringResource(R.string.tab_1),
                    StringResource(R.string.tab_2),
                    StringResource(R.string.tab_3),
                ),
                selectedTabIndex = selectedTabIndex,
                onTabClick = { selectedTabIndex = it },
            )
        }
    }
}