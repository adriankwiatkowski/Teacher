package com.example.teacher.feature.lesson.gradetemplate.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.mapResult
import com.example.teacher.core.data.repository.gradetemplate.GradeTemplateRepository
import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.feature.lesson.nav.LessonNavigation
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
    private val gradesResult: StateFlow<Result<List<BasicGradeTemplate>>> = lessonId
        .flatMapLatest { lessonId -> repository.getGradeTemplatesByLessonId(lessonId) }
        .stateIn(initialValue = Result.Loading)

    val gradeTemplateUiStateResult: StateFlow<Result<GradeTemplatesUiState>> = gradesResult
        .mapResult { grades ->
            val firstTermGrades = grades.filter { grade -> grade.isFirstTerm }
            val secondTermGrades = grades.filter { grade -> !grade.isFirstTerm }
            Result.Success(
                GradeTemplatesUiState(
                    firstTermGrades = firstTermGrades,
                    secondTermGrades = secondTermGrades,
                )
            )
        }
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