package com.example.teacher.feature.schedule.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.data.repository.event.EventRepository
import com.example.teacher.core.model.data.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
internal class ScheduleViewModel @Inject constructor(
    repository: EventRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val date: StateFlow<LocalDate> = savedStateHandle
        .getStateFlow(DATE_KEY, TimeUtils.encodeLocalDate(TimeUtils.currentDate()))
        .mapLatest { encodedDate -> TimeUtils.decodeLocalDate(encodedDate) }
        .stateIn(TimeUtils.currentDate())

    @OptIn(ExperimentalCoroutinesApi::class)
    val eventsResult: StateFlow<Result<List<Event>>> = date
        .flatMapLatest { date -> repository.getEvents(date) }
        .stateIn(initialValue = Result.Loading)

    fun onDateSelected(date: LocalDate) {
        setDate(date)
    }

    fun onPrevDateClick() {
        setDate(TimeUtils.minusDays(date.value, 1))
    }

    fun onNextDateClick() {
        setDate(TimeUtils.plusDays(date.value, 1))
    }

    private fun setDate(date: LocalDate) {
        savedStateHandle[DATE_KEY] = TimeUtils.encodeLocalDate(date)
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val DATE_KEY = "date"
    }
}