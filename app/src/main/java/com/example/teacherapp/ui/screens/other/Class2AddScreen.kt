package com.example.teacherapp.ui.screens.other

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun Class2AddScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(8.dp)) {
        var subject by remember { mutableStateOf("") }

        Text("Subject")
        TextField(value = subject, onValueChange = { subject = it }, singleLine = true)

        Text("Custom name (optional)")
        TextField(value = subject, onValueChange = { subject = it }, singleLine = true)

        Text("Class")
        TextField(value = subject, onValueChange = { subject = it }, singleLine = true)

        val suggestions = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
        var selectedSuggestion by remember { mutableStateOf("") }
        Text("Autocomplete")
//        FormAutoCompleteOutlinedTextField(
//            value = selectedSuggestion,
//            onValueChange = { selectedSuggestion = it },
//            suggestions = suggestions,
//        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Class2AddScreenPreview() {
    TeacherAppTheme {
        Surface {
            Class2AddScreen()
        }
    }
}