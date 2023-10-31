package com.example.teacher.feature.grade.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.feature.grade.data.GradeScoreData
import com.example.teacher.feature.grade.data.GradeScoreDataProvider
import java.math.BigDecimal

@Composable
internal fun GradeInputForm(
    isCalculateFromScoreForm: Boolean,
    gradeScoreData: GradeScoreData,
    onGradeChange: (grade: BigDecimal?) -> Unit,
    onGradeScoreThresholdChange: (grade: BigDecimal, newMinThreshold: Int) -> Unit,
    onMaxScoreChange: (maxScore: String?) -> Unit,
    onStudentScoreChange: (studentScore: String?) -> Unit,
    onSaveGradeScore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isCalculateFromScoreForm) {
        GradeScoreInput(
            gradeScoreData = gradeScoreData,
            onGradeScoreThresholdChange = onGradeScoreThresholdChange,
            onMaxScoreChange = onMaxScoreChange,
            onStudentScoreChange = onStudentScoreChange,
            onSaveGradeScore = onSaveGradeScore,
        )
    } else {
        GradeInputs(modifier = modifier, onGradeChange = onGradeChange)
    }
}

@Preview
@Composable
private fun GradeInputFormPreview() {
    TeacherTheme {
        Surface {
            var gradeScoreData by remember { mutableStateOf(GradeScoreDataProvider.createDefault()) }

            GradeInputForm(
                isCalculateFromScoreForm = false,
                gradeScoreData = gradeScoreData,
                onGradeChange = {},
                onGradeScoreThresholdChange = { grade, minThreshold ->
                    gradeScoreData = GradeScoreDataProvider.validateGradeScores(
                        gradeScoreData = gradeScoreData,
                        grade = grade,
                        newMinThreshold = minThreshold,
                    )
                },
                onMaxScoreChange = { maxScore ->
                    gradeScoreData = gradeScoreData.copy(
                        maxScore = GradeScoreDataProvider.validateGradeScore(maxScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onStudentScoreChange = { studentScore ->
                    gradeScoreData = gradeScoreData.copy(
                        studentScore = GradeScoreDataProvider.validateGradeScore(studentScore)
                    )
                    gradeScoreData = GradeScoreDataProvider.calculateGrade(gradeScoreData)
                },
                onSaveGradeScore = {},
            )
        }
    }
}