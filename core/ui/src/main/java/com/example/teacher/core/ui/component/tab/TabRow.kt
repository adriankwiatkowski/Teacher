package com.example.teacher.core.ui.component.tab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.model.TeacherIcon
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme

@Composable
fun TeacherTabRow(
    tabs: List<TeacherIcon>,
    selectedTabIndex: Int,
    onTabClick: (studentTabIndex: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // TODO: Fix divider doesn't fill full width.
        ScrollableTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = selectedTabIndex,
            // TODO: Use primary indicator when it becomes available.
        ) {
            tabs.forEachIndexed { index, tab ->
                TeacherTab(
                    text = stringResource(tab.text),
                    icon = tab.icon,
                    contentDescription = null,
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
                    TeacherIcons.details(text = R.string.ui_tab_1),
                    TeacherIcons.grades(text = R.string.ui_tab_2),
                    TeacherIcons.notes(text = R.string.ui_tab_3),
                ),
                selectedTabIndex = selectedTabIndex,
                onTabClick = { selectedTabIndex = it },
            )
        }
    }
}