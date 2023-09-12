package com.example.teacher.feature.schedule.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.lesson.LessonRepository
import com.example.teacher.core.model.data.LessonsByYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class LessonPickerViewModel @Inject constructor(
    repository: LessonRepository,
) : ViewModel() {

    val lessonsByYearResult: StateFlow<Result<List<LessonsByYear>>> = repository
        .getLessonsByYear()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )
}