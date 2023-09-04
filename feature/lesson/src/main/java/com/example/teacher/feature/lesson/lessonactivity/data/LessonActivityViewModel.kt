package com.example.teacher.feature.lesson.lessonactivity.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.lessonactivity.LessonActivityRepository
import com.example.teacher.core.model.data.LessonActivity
import com.example.teacher.feature.lesson.nav.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LessonActivityViewModel @Inject constructor(
    private val repository: LessonActivityRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonActivitiesResult: StateFlow<Result<List<LessonActivity>>> = lessonId
        .flatMapLatest { lessonId -> repository.getLessonActivitiesByLessonId(lessonId) }
        .stateIn(initialValue = Result.Loading)

    fun onDecreaseLessonActivity(lessonActivity: LessonActivity) {
        viewModelScope.launch {
            repository.decreaseLessonActivity(lessonActivity)
        }
    }

    fun onIncreaseLessonActivity(lessonActivity: LessonActivity) {
        viewModelScope.launch {
            repository.increaseLessonActivity(lessonActivity)
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
    }
}