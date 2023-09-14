package com.example.teacher.feature.lesson.attendance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.model.data.StudentWithAttendance
import com.example.teacher.core.ui.component.result.ResultContent
import com.example.teacher.core.ui.paramprovider.StudentsWithAttendancePreviewParameterProvider
import com.example.teacher.core.ui.theme.TeacherTheme
import com.example.teacher.core.ui.theme.spacing
import com.example.teacher.feature.lesson.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AttendanceStatisticsDialog(
    studentsWithAttendanceResult: Result<List<StudentWithAttendance>>,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(modifier = modifier, onDismissRequest = onDismissRequest) {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.padding(MaterialTheme.spacing.small),
                    text = stringResource(R.string.lesson_attendance_statistics_title),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))

                ResultContent(result = studentsWithAttendanceResult) { studentsWithAttendance ->
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(MaterialTheme.spacing.small),
                    ) {
                        items(
                            studentsWithAttendance,
                            key = { student -> student.student.id },
                        ) { student ->
                            val studentName = student.student.fullName
                            val averagePercent =
                                DecimalUtils.toLiteral(student.averageAttendancePercentage)

                            ListItem(
                                headlineContent = {
                                    Text("$studentName - $averagePercent%")
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AttendanceStatisticsDialogPreview(
    @PreviewParameter(
        StudentsWithAttendancePreviewParameterProvider::class,
        limit = 1,
    ) studentsWithAttendance: List<StudentWithAttendance>,
) {
    TeacherTheme {
        Surface {
            AttendanceStatisticsDialog(
                studentsWithAttendanceResult = Result.Success(studentsWithAttendance),
                onDismissRequest = {},
            )
        }
    }
}