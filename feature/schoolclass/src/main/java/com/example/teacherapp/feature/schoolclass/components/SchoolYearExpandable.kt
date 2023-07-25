package com.example.teacherapp.feature.schoolclass.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacherapp.core.common.utils.format
import com.example.teacherapp.core.model.data.SchoolYear
import com.example.teacherapp.core.ui.component.expandablelist.expandableItem
import com.example.teacherapp.core.ui.paramprovider.SchoolYearPreviewParameterProvider
import com.example.teacherapp.core.ui.theme.TeacherAppTheme
import com.example.teacherapp.core.ui.theme.spacing
import java.time.LocalDate

internal fun LazyListScope.schoolYearExpandable(
    schoolYear: SchoolYear,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
) {
    expandableItem(
        modifier = modifier,
        label = schoolYear.name,
        expanded = expanded,
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text(text = schoolYear.name)

            Spacer(Modifier.padding(MaterialTheme.spacing.small))

            val firstTerm = schoolYear.firstTerm
            TermItem(
                name = firstTerm.name,
                startDate = firstTerm.startDate,
                endDate = firstTerm.endDate,
            )

            Spacer(Modifier.padding(MaterialTheme.spacing.small))

            val secondTerm = schoolYear.secondTerm
            TermItem(
                name = secondTerm.name,
                startDate = secondTerm.startDate,
                endDate = secondTerm.endDate,
            )
        }
    }
}

@Composable
private fun TermItem(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Semestr $name")

        Row {
            Text("Rozpoczęcie semestru: ")
            Text(startDate.format())
        }
        Row {
            Text("Zakończenie semestru: ")
            Text(endDate.format())
        }
    }
}

@Preview
@Composable
private fun TermItemPreview(
    @PreviewParameter(SchoolYearPreviewParameterProvider::class) schoolYear: SchoolYear,
) {
    TeacherAppTheme {
        Surface {
            val term = schoolYear.firstTerm
            TermItem(
                name = term.name,
                startDate = term.startDate,
                endDate = term.endDate,
            )
        }
    }
}

@Preview
@Composable
private fun SchoolYearExpandablePreview(
    @PreviewParameter(SchoolYearPreviewParameterProvider::class) schoolYear: SchoolYear,
) {
    TeacherAppTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }
            LazyColumn {
                schoolYearExpandable(
                    schoolYear = schoolYear,
                    expanded = expanded,
                )
            }
        }
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SchoolYearExpandableDarkPreview(
    @PreviewParameter(SchoolYearPreviewParameterProvider::class) schoolYear: SchoolYear,
) {
    TeacherAppTheme {
        Surface {
            val expanded = remember { mutableStateOf(true) }
            LazyColumn {
                schoolYearExpandable(
                    schoolYear = schoolYear,
                    expanded = expanded,
                )
            }
        }
    }
}