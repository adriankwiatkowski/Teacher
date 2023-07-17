package com.example.teacherapp.ui.screens.schoolclass.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.form.FormAutoCompleteOutlinedTextField
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton

@Composable
fun SchoolYearInput(
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    onAddSchoolYear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (schoolYears.isEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.error) {
                    Text(
                        modifier = Modifier.weight(9f),
                        text = "Żeby stworzyć klasę musisz najpierw dodać rok szkolny.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Icon(
                        modifier = Modifier.weight(1f, fill = false),
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                    )
                }
            }
        } else {
            val stringifySchoolYear = { schoolYear: SchoolYear? ->
                if (schoolYear != null) {
                    "${schoolYear.name} (${schoolYear.firstTerm.name}, ${schoolYear.secondTerm.name})"
                } else {
                    ""
                }
            }

            FormAutoCompleteOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                inputField = schoolYear,
                onValueChange = {},
                onSuggestionSelect = onSchoolYearChange,
                suggestions = schoolYears,
                inputToString = { stringifySchoolYear(it.value) },
                suggestionToString = stringifySchoolYear,
                label = "Rok szkolny",
                readOnly = true,
            )
        }

        TeacherOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddSchoolYear,
        ) {
            Text(text = "Dodaj nowy rok szkolny")
        }
    }
}