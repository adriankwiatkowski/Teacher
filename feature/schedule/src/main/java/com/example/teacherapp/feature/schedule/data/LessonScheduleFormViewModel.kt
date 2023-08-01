package com.example.teacherapp.feature.schedule.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.lessonschedule.LessonScheduleRepository
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.model.data.LessonScheduleType
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.schedule.nav.ScheduleNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
internal class LessonScheduleFormViewModel @Inject constructor(
    private val repository: LessonScheduleRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonResult: StateFlow<Result<Lesson>> = lessonId
        .flatMapLatest { lessonId -> repository.getLessonById(lessonId) }
        .stateIn(Result.Loading)

    var form by mutableStateOf(LessonScheduleFormProvider.createDefaultForm())
        private set

    fun onDateChange(date: LocalDate) {
        form = form.copy(date = LessonScheduleFormProvider.sanitizeDate(date))
    }

    fun onStartTimeChange(startTime: LocalTime) {
        val timeData = LessonScheduleFormProvider.sanitizeStartTime(startTime, form.endTime)
        form = form.copy(
            startTime = timeData.startTime,
            endTime = timeData.endTime,
        )
    }

    fun onEndTimeChange(endTime: LocalTime) {
        val timeData = LessonScheduleFormProvider.sanitizeEndTime(form.startTime, endTime)
        form = form.copy(
            startTime = timeData.startTime,
            endTime = timeData.endTime,
        )
    }

    fun onTypeChange(type: LessonScheduleType) {
        form = form.copy(type = type)
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertLessonSchedule(
                lessonId = lessonId.value,
                date = form.date,
                startTime = form.startTime,
                endTime = form.endTime,
                type = form.type,
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
            }
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val LESSON_ID_KEY = ScheduleNavigation.lessonIdArg
    }
}