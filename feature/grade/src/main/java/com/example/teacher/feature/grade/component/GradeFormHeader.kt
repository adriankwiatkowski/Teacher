package com.example.teacher.feature.grade.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.GradeTemplateInfo
import com.example.teacher.core.ui.component.TeacherInputChip
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.grade.R
import java.math.BigDecimal

@Composable
internal fun GradeFormHeader(
    student: BasicStudent,
    gradeInfo: GradeTemplateInfo,
    initialGrade: BigDecimal?,
    inputGrade: BigDecimal?,
    isCalculateFromScoreForm: Boolean,
    onIsCalculateFromScoreFormChange: (isCalculateFromScoreForm: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val gradeName = gradeInfo.gradeName
    val gradeTermName = if (gradeInfo.isFirstTerm) {
        gradeInfo.lesson.schoolClass.schoolYear.firstTerm.name
    } else {
        gradeInfo.lesson.schoolClass.schoolYear.secondTerm.name
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(),
    ) {
        Column(modifier = Modifier.padding(MaterialTheme.spacing.small)) {
            Text(
                modifier = modifier,
                text = student.fullName,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(stringResource(R.string.grades_grade_with_term, gradeName, gradeTermName))
            Text(stringResource(R.string.grade_grade_weight, gradeInfo.gradeWeight))
            if (initialGrade != null) {
                Text(stringResource(R.string.grade_current_grade, toGradeWithLiteral(initialGrade)))
            }

            // Don't show new grade if it's same as old one.
            if (initialGrade == null || (initialGrade != inputGrade)) {
                Text(stringResource(R.string.grade_new_grade, toGradeWithLiteral(inputGrade)))
            }

            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))

            GradeFormTypeInput(
                isCalculateFromScoreForm = isCalculateFromScoreForm,
                onIsCalculateFromScoreFormChange = onIsCalculateFromScoreFormChange,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun GradeFormTypeInput(
    isCalculateFromScoreForm: Boolean,
    onIsCalculateFromScoreFormChange: (isCalculateFromScoreForm: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.Center,
    ) {
        TeacherInputChip(
            label = stringResource(R.string.grades_grade_button_input),
            selected = !isCalculateFromScoreForm,
            onClick = { onIsCalculateFromScoreFormChange(false) },
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        TeacherInputChip(
            label = stringResource(R.string.grades_grade_score_input),
            selected = isCalculateFromScoreForm,
            onClick = { onIsCalculateFromScoreFormChange(true) },
        )
    }
}

@Composable
private fun toGradeWithLiteral(grade: BigDecimal?): String {
    return if (grade != null) {
        stringResource(
            R.string.grade_grade_with_literal,
            DecimalUtils.toGrade(grade),
            DecimalUtils.toLiteral(grade),
        )
    } else {
        stringResource(R.string.grade_no_grade)
    }
}