package com.example.teacherapp.feature.schedule.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.lessonschedule.LessonScheduleRepository
import com.example.teacherapp.core.model.data.LessonSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class ScheduleViewModel @Inject constructor(
    repository: LessonScheduleRepository,
) : ViewModel() {

    val lessonSchedulesResult: StateFlow<Result<List<LessonSchedule>>> = repository
        .getLessonSchedules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )
}