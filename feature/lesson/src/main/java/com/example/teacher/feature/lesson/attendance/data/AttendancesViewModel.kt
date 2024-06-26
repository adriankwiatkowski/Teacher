package com.example.teacher.feature.lesson.attendance.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.combineResult
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.data.repository.lessonattendance.LessonAttendanceRepository
import com.example.teacher.core.model.data.LessonEventAttendance
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.StudentWithAttendance
import com.example.teacher.feature.lesson.nav.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class AttendancesViewModel @Inject constructor(
    repository: LessonAttendanceRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val attendancesResult: StateFlow<Result<List<LessonEventAttendance>>> = lessonId
        .flatMapLatest { lessonId -> repository.getLessonEventAttendancesByLessonId(lessonId) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val schoolYearResult: StateFlow<Result<SchoolYear>> = lessonId
        .flatMapLatest { lessonId -> repository.getSchoolYearByLessonId(lessonId) }
        .stateIn(initialValue = Result.Loading)

    val attendancesUiStateResult: StateFlow<Result<AttendancesUiState>> = attendancesResult
        .combineResult(schoolYearResult) { attendances, schoolYear ->
            val firstTermScheduleAttendances = attendances.filter { attendance ->
                val term = schoolYear.firstTerm
                TimeUtils.isBetween(attendance.date, term.startDate, term.endDate)
            }
            val secondTermScheduleAttendances = attendances.filter { attendance ->
                val term = schoolYear.secondTerm
                TimeUtils.isBetween(attendance.date, term.startDate, term.endDate)
            }
            val scheduleAttendancesWithoutTerm = attendances.filter { attendance ->
                val isInFirstTerm = attendance in firstTermScheduleAttendances
                val isInSecondTerm = attendance in secondTermScheduleAttendances
                !isInFirstTerm && !isInSecondTerm
            }

            Result.Success(
                AttendancesUiState(
                    firstTermScheduleAttendances = firstTermScheduleAttendances,
                    secondTermScheduleAttendances = secondTermScheduleAttendances,
                    scheduleAttendancesWithoutTerm = scheduleAttendancesWithoutTerm,
                )
            )
        }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentsWithAttendanceResult: StateFlow<Result<List<StudentWithAttendance>>> = lessonId
        .flatMapLatest { lessonId -> repository.getStudentsWithAttendanceByLessonId(lessonId) }
        .stateIn(initialValue = Result.Loading)

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
    }
}