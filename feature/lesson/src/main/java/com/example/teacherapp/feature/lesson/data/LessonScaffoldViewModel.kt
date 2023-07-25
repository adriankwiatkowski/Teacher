package com.example.teacherapp.feature.lesson.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.lesson.LessonRepository
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.feature.lesson.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LessonScaffoldViewModel @Inject constructor(
    private val repository: LessonRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)
    val isLessonDeleted = savedStateHandle.getStateFlow(IS_LESSON_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentResult: StateFlow<Result<Lesson>> = lessonId
        .flatMapLatest { lessonId -> repository.getLessonById(lessonId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    fun onDeleteLesson() {
        if (isLessonDeleted.value) {
            return
        }

        viewModelScope.launch {
            repository.deleteLessonById(lessonId.value)
            savedStateHandle[IS_LESSON_DELETED_KEY] = true
        }
    }

    companion object {
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
        private const val IS_LESSON_DELETED_KEY = "is-lesson-deleted"
    }
}