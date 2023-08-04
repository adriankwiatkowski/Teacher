package com.example.teacherapp.feature.lesson.attendance.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.lessonattendance.LessonAttendanceRepository
import com.example.teacherapp.core.model.data.Attendance
import com.example.teacherapp.core.model.data.LessonAttendance
import com.example.teacherapp.core.model.data.LessonSchedule
import com.example.teacherapp.feature.lesson.nav.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AttendanceFormViewModel @Inject constructor(
    private val repository: LessonAttendanceRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonScheduleId = savedStateHandle.getStateFlow(LESSON_SCHEDULE_ID_KEY, 0L)

    private val _dialogState = MutableStateFlow<DialogState?>(null)
    val dialogState = _dialogState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonAttendancesResult: StateFlow<Result<List<LessonAttendance>>> = lessonScheduleId
        .flatMapLatest { id -> repository.getLessonAttendancesByLessonScheduleId(id) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonScheduleResult: StateFlow<Result<LessonSchedule>> = lessonScheduleId
        .flatMapLatest { id -> repository.getLessonScheduleById(id) }
        .stateIn(initialValue = Result.Loading)

    fun onLessonAttendanceClick(lessonAttendance: LessonAttendance) {
        _dialogState.value = DialogState(
            studentId = lessonAttendance.student.id,
            studentFullName = lessonAttendance.student.fullName,
            attendance = lessonAttendance.attendance,
        )
    }

    fun onAttendanceSelect(attendance: Attendance?) {
        updateDialogState { dialogState ->
            dialogState.copy(attendance = attendance)
        }
    }

    fun onAttendanceDismissRequest() {
        _dialogState.value = null
    }

    fun onAttendanceConfirmClick() {
        val dialogState = dialogState.value ?: return

        val lessonScheduleId = lessonScheduleId.value
        val studentId = dialogState.studentId
        val attendance = dialogState.attendance

        viewModelScope.launch {
            if (attendance != null) {
                repository.insertOrUpdateLessonAttendance(
                    lessonScheduleId = lessonScheduleId,
                    studentId = studentId,
                    attendance = attendance,
                )
            } else {
                repository.deleteLessonAttendance(
                    lessonScheduleId = lessonScheduleId,
                    studentId = studentId,
                )
            }
        }
    }

    private fun updateDialogState(block: (dialogState: DialogState) -> DialogState) {
        _dialogState.update { dialogState ->
            dialogState?.run(block)
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val LESSON_SCHEDULE_ID_KEY = LessonNavigation.lessonScheduleIdArg
    }
}

internal data class DialogState(
    val studentId: Long,
    val studentFullName: String,
    val attendance: Attendance?,
)