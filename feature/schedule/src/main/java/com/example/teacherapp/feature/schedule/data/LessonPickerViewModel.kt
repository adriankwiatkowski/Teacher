package com.example.teacherapp.feature.schedule.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.lesson.LessonRepository
import com.example.teacherapp.core.model.data.Lesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class LessonPickerViewModel @Inject constructor(
    repository: LessonRepository,
) : ViewModel() {

    val lessonsResult: StateFlow<Result<List<Lesson>>> = repository
        .getLessons()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )
}