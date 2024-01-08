package com.example.teacher.feature.lesson.attendance.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.lessonattendance.LessonAttendanceRepository
import com.example.teacher.core.model.data.Attendance
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.LessonAttendance
import com.example.teacher.feature.lesson.nav.LessonNavigation
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
internal class AttendanceViewModel @Inject constructor(
    private val repository: LessonAttendanceRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val eventId = savedStateHandle.getStateFlow(EVENT_ID_KEY, 0L)

    private val _dialogState = MutableStateFlow<DialogState?>(null)
    val dialogState = _dialogState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonAttendancesResult: StateFlow<Result<List<LessonAttendance>>> = eventId
        .flatMapLatest { id -> repository.getLessonAttendancesByEventId(id) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val eventResult: StateFlow<Result<Event>> = eventId
        .flatMapLatest { id -> repository.getEventById(id) }
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

        val eventId = eventId.value
        val studentId = dialogState.studentId
        val attendance = dialogState.attendance

        viewModelScope.launch {
            if (attendance != null) {
                repository.upsertLessonAttendance(
                    eventId = eventId,
                    studentId = studentId,
                    attendance = attendance,
                )
            } else {
                repository.deleteLessonAttendance(
                    eventId = eventId,
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
        private const val EVENT_ID_KEY = LessonNavigation.eventIdArg
    }
}