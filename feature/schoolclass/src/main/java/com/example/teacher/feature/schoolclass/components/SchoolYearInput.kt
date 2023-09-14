package com.example.teacher.feature.schoolclass.components

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
import androidx.compose.ui.res.stringResource
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.form.FormAutoCompleteTextField
import com.example.teacher.core.ui.model.InputField
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.feature.schoolclass.R

@Composable
internal fun SchoolYearInput(
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    onAddSchoolYear: () -> Unit,
    onEditSchoolYear: () -> Unit,
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
                        text = stringResource(R.string.school_class_no_school_year),
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

            FormAutoCompleteTextField(
                modifier = Modifier.fillMaxWidth(),
                inputField = schoolYear,
                onValueChange = {},
                onSuggestionSelect = onSchoolYearChange,
                suggestions = schoolYears,
                inputToString = { stringifySchoolYear(it.value) },
                suggestionToString = stringifySchoolYear,
                label = stringResource(R.string.school_class_school_year),
                readOnly = true,
            )
        }

        if (schoolYear.value != null) {
            TeacherButton(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(R.string.school_class_edit_school_year),
                icon = TeacherIcons.edit(),
                onClick = onEditSchoolYear,
            )
        }

        TeacherButton(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.school_class_add_school_year),
            icon = TeacherIcons.add(),
            onClick = onAddSchoolYear,
        )
    }
}