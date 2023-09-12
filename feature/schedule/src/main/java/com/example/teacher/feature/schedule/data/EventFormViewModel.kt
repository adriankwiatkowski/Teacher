package com.example.teacher.feature.schedule.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.event.EventRepository
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.EventType
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.schedule.nav.ScheduleNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
internal class EventFormViewModel @Inject constructor(
    private val repository: EventRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val eventId = savedStateHandle.getStateFlow(EVENT_ID_KEY, DEFAULT_ID)
    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, DEFAULT_ID)
    val isDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val eventResult: StateFlow<Result<Event?>> = eventId
        .flatMapLatest { eventId -> repository.getEventOrNullById(eventId) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonResult: StateFlow<Result<Lesson?>> = lessonId
        .flatMapLatest { lessonId -> repository.getLessonOrNullById(lessonId) }
        .stateIn(Result.Loading)

    private val _isLessonForm = MutableStateFlow(true)
    val isLessonForm = _isLessonForm.asStateFlow()

    var form by mutableStateOf(EventFormProvider.createDefaultForm())
        private set

    init {
        combine(lessonId, isLessonForm) { lessonId, isLessonForm ->
            !isLessonForm || lessonId != DEFAULT_ID
        }.onEach { isValid -> form = form.copy(isValid = isValid) }.launchIn(viewModelScope)

        eventResult
            .onEach { eventResult ->
                val event = (eventResult as? Result.Success)?.data ?: return@onEach

                val lessonId = event.lesson?.id
                setLessonId(lessonId ?: DEFAULT_ID)
                _isLessonForm.value = lessonId != null

                form = form.copy(
                    date = event.date,
                    startTime = event.startTime,
                    endTime = event.endTime,
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onDayChange(day: DayOfWeek) {
        form = form.copy(day = EventFormProvider.sanitizeDay(day))
    }

    fun onDateChange(date: LocalDate) {
        form = form.copy(date = EventFormProvider.sanitizeDate(date))
    }

    fun onStartTimeChange(startTime: LocalTime) {
        val timeData = EventFormProvider.sanitizeStartTime(startTime, form.endTime)
        form = form.copy(
            startTime = timeData.startTime,
            endTime = timeData.endTime,
        )
    }

    fun onEndTimeChange(endTime: LocalTime) {
        val timeData = EventFormProvider.sanitizeEndTime(form.startTime, endTime)
        form = form.copy(
            startTime = timeData.startTime,
            endTime = timeData.endTime,
        )
    }

    fun onTermSelected(isFirstTermSelected: Boolean) {
        form = form.copy(isFirstTermSelected = isFirstTermSelected)
    }

    fun onTypeChange(type: EventType) {
        form = form.copy(type = type)
    }

    fun onIsLessonFormChange(isLessonForm: Boolean) {
        _isLessonForm.value = isLessonForm
    }

    fun setLessonId(lessonId: Long) {
        savedStateHandle[LESSON_ID_KEY] = lessonId
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        if (isLessonForm.value && lessonId.value == DEFAULT_ID) {
            return
        }
        val eventId = eventId.value
        val lessonId = lessonId.value

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            if (eventId != DEFAULT_ID) {
                repository.updateEvent(
                    id = eventId,
                    lessonId = if (isLessonForm.value) lessonId else null,
                    date = form.date,
                    startTime = form.startTime,
                    endTime = form.endTime,
                )
            } else if (isLessonForm.value) {
                repository.insertLessonSchedule(
                    lessonId = lessonId,
                    day = form.day,
                    date = form.date,
                    startTime = form.startTime,
                    endTime = form.endTime,
                    isFirstTermSelected = form.isFirstTermSelected,
                    type = form.type,
                )
            } else {
                repository.insertEvent(
                    date = form.date,
                    startTime = form.startTime,
                    endTime = form.endTime,
                    type = form.type,
                )
            }

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            repository.deleteEventById(eventId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val EVENT_ID_KEY = ScheduleNavigation.eventIdArg
        private const val LESSON_ID_KEY = ScheduleNavigation.lessonIdArg
        private const val DEFAULT_ID = 0L
        private const val IS_DELETED_KEY = "is-deleted"
    }
}