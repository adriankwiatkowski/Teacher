package com.example.teacherapp.feature.lesson.note.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.lessonnote.LessonNoteRepository
import com.example.teacherapp.core.model.data.BasicLessonNote
import com.example.teacherapp.feature.lesson.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class NotesViewModel @Inject constructor(
    private val repository: LessonNoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonNotesResult: StateFlow<Result<List<BasicLessonNote>>> = lessonId
        .flatMapLatest { lessonId -> repository.getLessonNotesByLessonId(lessonId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    companion object {
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
    }
}