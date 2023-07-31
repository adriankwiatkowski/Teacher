package com.example.teacherapp.feature.lesson.gradetemplate.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.gradetemplate.GradeTemplateRepository
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.feature.lesson.nav.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class GradeTemplatesViewModel @Inject constructor(
    private val repository: GradeTemplateRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val gradesResult: StateFlow<Result<List<BasicGradeTemplate>>> = lessonId
        .flatMapLatest { lessonId -> repository.getGradeTemplatesByLessonId(lessonId) }
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