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
    tabs: List<StudentTab>,
    selectedTab: StudentTab,
    selectedTabIndex: Int,
    onTabClick: (studentTab: StudentTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
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
            val tabs = remember { listOf(StudentTab.Grades, StudentTab.Detail, StudentTab.Notes) }
            var selectedTab by remember { mutableStateOf(StudentTab.Detail) }
            val selectedTabIndex = remember(selectedTab) {
                tabs.indexOf(selectedTab)
            }

            StudentTabRow(
                tabs = tabs,
                selectedTab = selectedTab,
                selectedTabIndex = selectedTabIndex,
                onTabClick = { selectedTab = it }
            )
        }
    }
}