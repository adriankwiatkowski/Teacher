package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun StudentTabRow(
    selectedTab: StudentTab,
    onTabClick: (studentTab: StudentTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val tabs = remember {
            listOf(StudentTab.Detail, StudentTab.Grades, StudentTab.Notes)
        }
        val selectedTabIndex = remember(selectedTab) {
            tabs.indexOf(selectedTab)
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEach { studentTab ->
                StudentTab(
                    text = studentTab.title,
                    selected = studentTab == selectedTab,
                    onClick = { onTabClick(studentTab) },
                )
            }
        }
    }
}

@Composable
private fun StudentTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Tab(
        modifier = modifier,
        text = { Text(text) },
        selected = selected,
        onClick = onClick,
    )
}

@Preview
@Composable
private fun StudentTabBarPreview() {
    TeacherAppTheme {
        Surface {
            var selectedTab by remember { mutableStateOf(StudentTab.Detail) }

            StudentTabRow(
                selectedTab = selectedTab,
                onTabClick = { selectedTab = it }
            )
        }
    }
}