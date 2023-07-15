package com.example.teacherapp.ui.screens.gradetemplate.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GradeTemplatesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)

    // TODO: Actually fetch from repository.
    @OptIn(ExperimentalCoroutinesApi::class)
    val gradesResult: StateFlow<Result<List<BasicGradeTemplate>>> = lessonId
        .flatMapLatest { lessonId -> flowOf(Result.Success(emptyList<BasicGradeTemplate>())) }
        .stateIn(initialValue = Result.Loading)

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
    }
}