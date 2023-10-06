package com.example.teacher.feature.schedule.data

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

    private val _form = MutableStateFlow(EventFormProvider.createDefaultForm())
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    init {
        combine(lessonId, isLessonForm) { lessonId, isLessonForm ->
            !isLessonForm || lessonId != DEFAULT_ID
        }.onEach { isValid ->
            _form.value = form.value.copy(isValid = isValid)
            initialForm.value = initialForm.value.copy(isValid = isValid)
        }.launchIn(viewModelScope)

        eventResult
            .onEach { eventResult ->
                val event = (eventResult as? Result.Success)?.data ?: return@onEach

                val lessonId = event.lesson?.id
                setLessonId(lessonId ?: DEFAULT_ID)
                _isLessonForm.value = lessonId != null

                _form.value = form.value.copy(
                    date = event.date,
                    startTime = event.startTime,
                    endTime = event.endTime,
                    isCancelled = event.isCancelled,
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onDayChange(day: DayOfWeek) {
        _form.value = form.value.copy(day = EventFormProvider.sanitizeDay(day))
    }

    fun onDateChange(date: LocalDate) {
        _form.value = form.value.copy(date = EventFormProvider.sanitizeDate(date))
    }

    fun onStartTimeChange(startTime: LocalTime) {
        val timeData = EventFormProvider.sanitizeStartTime(startTime, form.value.endTime)
        _form.value = form.value.copy(
            startTime = timeData.startTime,
            endTime = timeData.endTime,
        )
    }

    fun onEndTimeChange(endTime: LocalTime) {
        val timeData = EventFormProvider.sanitizeEndTime(form.value.startTime, endTime)
        _form.value = form.value.copy(
            startTime = timeData.startTime,
            endTime = timeData.endTime,
        )
    }

    fun onTermSelected(isFirstTermSelected: Boolean) {
        _form.value = form.value.copy(isFirstTermSelected = isFirstTermSelected)
    }

    fun onTypeChange(type: EventType) {
        _form.value = form.value.copy(type = type)
    }

    fun onIsCancelledChange(isCancelled: Boolean) {
        _form.value = form.value.copy(isCancelled = isCancelled)
    }

    fun onIsLessonFormChange(isLessonForm: Boolean) {
        _isLessonForm.value = isLessonForm
    }

    fun setLessonId(lessonId: Long) {
        savedStateHandle[LESSON_ID_KEY] = lessonId
    }

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        if (isLessonForm.value && lessonId.value == DEFAULT_ID) {
            return
        }
        val eventId = eventId.value
        val lessonId = lessonId.value

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            if (eventId != DEFAULT_ID) {
                repository.updateEvent(
                    id = eventId,
                    lessonId = if (isLessonForm.value) lessonId else null,
                    date = form.date,
                    startTime = form.startTime,
                    endTime = form.endTime,
                    isCancelled = form.isCancelled,
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
                    isCancelled = form.isCancelled,
                )
            } else {
                repository.insertEvent(
                    date = form.date,
                    startTime = form.startTime,
                    endTime = form.endTime,
                    type = form.type,
                    isCancelled = form.isCancelled,
                )
            }

            if (isActive) {
                _form.value = form.copy(status = FormStatus.Success)
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