package com.example.teacher.feature.grade.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import java.math.BigDecimal

@Composable
internal fun GradeInputs(
    onGradeChange: (grade: BigDecimal?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GradeInputsRow(
            grades = listOf(DecimalUtils.One),
            onGradeClick = onGradeChange,
        )
        GradeInputsRow(
            grades = listOf(DecimalUtils.TwoMinus, DecimalUtils.Two, DecimalUtils.TwoPlus),
            onGradeClick = onGradeChange,
        )
        GradeInputsRow(
            grades = listOf(DecimalUtils.ThreeMinus, DecimalUtils.Three, DecimalUtils.ThreePlus),
            onGradeClick = onGradeChange,
        )
        GradeInputsRow(
            grades = listOf(DecimalUtils.FourMinus, DecimalUtils.Four, DecimalUtils.FourPlus),
            onGradeClick = onGradeChange,
        )
        GradeInputsRow(
            grades = listOf(DecimalUtils.FiveMinus, DecimalUtils.Five, DecimalUtils.FivePlus),
            onGradeClick = onGradeChange,
        )
        GradeInputsRow(
            grades = listOf(DecimalUtils.SixMinus, DecimalUtils.Six),
            onGradeClick = onGradeChange,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GradeInputsRow(
    grades: List<BigDecimal>,
    onGradeClick: (grade: BigDecimal?) -> Unit,
) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
        for (grade in grades) {
            GradeInput(grade = DecimalUtils.toGrade(grade), onClick = { onGradeClick(grade) })
        }
    }
}

@Composable
private fun GradeInput(
    grade: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TeacherButton(modifier = modifier, label = grade, onClick = onClick)
}

@Preview
@Composable
private fun GradeInputsPreview() {
    TeacherTheme {
        Surface {
            GradeInputs(onGradeChange = {})
        }
    }
}