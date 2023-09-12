package com.example.teacher.feature.schoolclass

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.ui.paramprovider.SchoolYearPreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import java.time.LocalDate

@Composable
internal fun SchoolClassDetailScreen(
    snackbarHostState: SnackbarHostState,
    schoolYear: SchoolYear,
    modifier: Modifier = Modifier,
) {
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { innerPadding ->
        MainScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(MaterialTheme.spacing.medium),
            schoolYear = schoolYear,
        )
    }
}

@Composable
private fun MainScreen(
    schoolYear: SchoolYear,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
    ) {
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

@Composable
private fun TermItem(
    name: String,
    startDate: LocalDate,
    endDate: LocalDate,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = stringResource(R.string.school_class_term_name, name))

        Row {
            Text(stringResource(R.string.school_class_term_start))
            Text(TimeUtils.format(startDate))
        }
        Row {
            Text(stringResource(R.string.school_class_term_end))
            Text(TimeUtils.format(endDate))
        }
    }
}

@Preview
@Composable
private fun TermItemPreview(
    @PreviewParameter(SchoolYearPreviewParameterProvider::class) schoolYear: SchoolYear,
) {
    TeacherTheme {
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
private fun SchoolClassDetailScreenPreview(
    @PreviewParameter(
        SchoolYearPreviewParameterProvider::class,
        limit = 1,
    ) schoolYear: SchoolYear,
) {
    TeacherTheme {
        Surface {
            SchoolClassDetailScreen(
                snackbarHostState = remember { SnackbarHostState() },
                schoolYear = schoolYear,
            )
        }
    }
}