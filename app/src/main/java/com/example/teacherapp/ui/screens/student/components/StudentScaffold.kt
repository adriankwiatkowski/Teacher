package com.example.teacherapp.ui.screens.student.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.nav.graphs.student.tab.StudentTab
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun StudentScaffold(
    isScaffoldVisible: Boolean,
    title: String,
    menuItems: List<ActionMenuItem>,
    showNavigationIcon: Boolean,
    onNavigationIconClick: () -> Unit,
    selectedTab: StudentTab,
    onTabClick: (studentTab: StudentTab) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (!isScaffoldVisible) {
        Box(modifier = modifier) {
            content()
        }
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TeacherTopBar(
                    title = title,
                    menuItems = menuItems,
                    showNavigationIcon = showNavigationIcon,
                    onNavigationIconClick = onNavigationIconClick,
                    visible = true
                )

                StudentTabRow(
                    selectedTab = selectedTab,
                    onTabClick = onTabClick,
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Preview
@Composable
private fun StudentScaffoldPreview() {
    TeacherAppTheme {
        Surface {
            var selectedTab by remember { mutableStateOf(StudentTab.Detail) }

            StudentScaffold(
                isScaffoldVisible = true,
                title = "Klasa 3A",
                menuItems = listOf(),
                showNavigationIcon = true,
                onNavigationIconClick = {},
                selectedTab = selectedTab,
                onTabClick = { selectedTab = it },
            ) {
                Column {
                    repeat(10) {
                        Text("Details of tab...")
                    }
                }
            }
        }
    }
}