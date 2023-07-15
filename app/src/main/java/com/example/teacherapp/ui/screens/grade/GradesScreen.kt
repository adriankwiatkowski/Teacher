package com.example.teacherapp.ui.screens.grade

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacherapp.data.provider.ActionMenuItemProvider
import com.example.teacherapp.ui.components.TeacherTopBar
import com.example.teacherapp.ui.theme.TeacherAppTheme
import com.example.teacherapp.ui.theme.spacing

@Composable
fun GradesScreen(
    showNavigationIcon: Boolean,
    onNavBack: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TeacherTopBar(
                title = "Polski 2A",
                showNavigationIcon = showNavigationIcon,
                onNavigationIconClick = onNavBack,
                menuItems = listOf(
                    ActionMenuItemProvider.edit(onEditClick),
                    ActionMenuItemProvider.delete(onDeleteClick),
                ),
            )
        }
    ) { innerPadding ->
        MainContent(
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(MaterialTheme.spacing.small),
    ) {
        item {
            GradeItem(fullName = "Jan Kowalski", grade = "6")
            Divider()
        }
        item {
            GradeItem(fullName = "Jan Kowalski", grade = "5")
            Divider()
        }
        item {
            GradeItem(fullName = "Jan Kowalski", grade = "4")
            Divider()
        }
        item {
            GradeItem(fullName = "Jan Kowalski", grade = "3")
            Divider()
        }
        item {
            GradeItem(fullName = "Jan Kowalski", grade = "2")
            Divider()
        }
        item {
            GradeItem(fullName = "Jan Kowalski", grade = "1")
            Divider()
        }
        item {
            GradeItem(fullName = "Jan Kowalski", grade = "brak oceny")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GradeItem(
    fullName: String,
    grade: String,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        text = { Text("$fullName ($grade)") }
//        text = { Text(fullName) },
//        secondaryText = { Text(grade) },
    )
}

@Preview
@Composable
private fun GradesScreenPreview() {
    TeacherAppTheme {
        Surface {
            GradesScreen(
                showNavigationIcon = true,
                onNavBack = {},
                onEditClick = {},
                onDeleteClick = {},
            )
        }
    }
}