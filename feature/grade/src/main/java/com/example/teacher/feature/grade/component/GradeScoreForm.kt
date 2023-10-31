package com.example.teacher.feature.grade.component

import android.view.KeyEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.ui.component.TeacherButton
import com.example.teacher.core.ui.component.TeacherIntSlider
import com.example.teacher.core.ui.component.TextWithIcon
import com.example.teacher.core.ui.component.form.FormTextField
import com.example.teacher.core.ui.provider.TeacherIcons
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.grade.R
import com.example.teacher.feature.grade.data.GradeScoreData
import com.example.teacher.feature.grade.data.GradeScoreDataProvider
import java.math.BigDecimal

@Composable
internal fun GradeScoreForm(
    gradeScoreData: GradeScoreData,
    onGradeScoreThresholdChange: (grade: BigDecimal, newMinThreshold: Int) -> Unit,
    onMaxScoreChange: (maxScore: String?) -> Unit,
    onStudentScoreChange: (studentScore: String?) -> Unit,
    onSaveGradeScore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val focusManager = LocalFocusManager.current
        val movePrev = { focusManager.moveFocus(FocusDirection.Up) }
        val moveNext = { focusManager.moveFocus(FocusDirection.Down) }

        val textFieldModifier = Modifier
            .fillMaxWidth()
            .onKeyEvent { keyEvent ->
                when (keyEvent.nativeKeyEvent.keyCode) {
                    KeyEvent.KEYCODE_ENTER,
                    KeyEvent.KEYCODE_TAB -> {
                        if (keyEvent.isShiftPressed) movePrev() else moveNext()
                        true
                    }

                    else -> false
                }
            }
        val commonKeyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number,
        )
        val commonKeyboardActions = KeyboardActions(onNext = { moveNext() })

        FormTextField(
            modifier = textFieldModifier,
            label = stringResource(R.string.grade_max_score_label),
            inputField = gradeScoreData.maxScore,
            onValueChange = onMaxScoreChange,
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
        FormTextField(
            modifier = textFieldModifier,
            label = stringResource(R.string.grade_student_score_label),
            inputField = gradeScoreData.studentScore,
            onValueChange = onStudentScoreChange,
            keyboardOptions = commonKeyboardOptions,
            keyboardActions = commonKeyboardActions,
        )
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

        if (gradeScoreData.calculatedGrade != null) {
            val grade = DecimalUtils.toGrade(gradeScoreData.calculatedGrade.grade)
            val percentage = gradeScoreData.calculatedGrade.percentage
            Text(
                text = stringResource(R.string.grade_from_score, grade, percentage),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
        }

        Divider()
        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

        TextWithIcon(
            text = stringResource(R.string.grade_score_thresholds_save_info),
            icon = TeacherIcons.info(),
        )
        ThresholdsSaveButton(onClick = onSaveGradeScore)

        val gradeThresholds = gradeScoreData.gradeScoreThresholds
        for (gradeThreshold in gradeThresholds) {
            key(gradeThreshold.grade.toString()) {
                ScoreSlider(
                    grade = gradeThreshold.grade,
                    gradeThreshold = gradeThreshold.minThreshold,
                    onGradeThresholdChange = { newMinThreshold ->
                        onGradeScoreThresholdChange(gradeThreshold.grade, newMinThreshold)
                    },
                )
            }
        }

        ThresholdsSaveButton(onClick = onSaveGradeScore)
    }
}

@Composable
private fun ScoreSlider(
    grade: BigDecimal,
    gradeThreshold: Int,
    onGradeThresholdChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    TeacherIntSlider(
        modifier = modifier,
        label = DecimalUtils.toGrade(grade),
        min = 0,
        max = 100,
        value = gradeThreshold,
        onValueChange = onGradeThresholdChange,
    )
}

@Composable
private fun ThresholdsSaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TeacherButton(
        modifier = modifier,
        label = stringResource(R.string.grade_score_thresholds_save),
        onClick = onClick,
    )
}

@Preview
@Composable
private fun GradeScoreFormPreview() {
    TeacherTheme {
        Surface {
            var gradeScoreData by remember { mutableStateOf(GradeScoreDataProvider.createDefault()) }

            GradeScoreForm(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                gradeScoreData = gradeScoreData,
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