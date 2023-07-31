package com.example.teacherapp.feature.grade.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.combineResult
import com.example.teacherapp.core.data.repository.grade.GradeRepository
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import com.example.teacherapp.feature.grade.nav.GradeNavigation
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
internal class GradesViewModel @Inject constructor(
    private val repository: GradeRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val gradeTemplateId = savedStateHandle.getStateFlow(GRADE_TEMPLATE_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val gradesResult: StateFlow<Result<List<BasicGradeForTemplate>>> = gradeTemplateId
        .flatMapLatest { gradeTemplateId -> repository.getGradesByGradeTemplateId(gradeTemplateId) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val gradeTemplateInfoResult: StateFlow<Result<GradeTemplateInfo>> = gradeTemplateId
        .flatMapLatest { gradeTemplateId ->
            repository.getGradeTemplateInfoByGradeTemplateId(
                gradeTemplateId
            )
        }
        .stateIn(initialValue = Result.Loading)

    val uiState: StateFlow<Result<GradesUiState>> = gradesResult
        .combineResult(gradeTemplateInfoResult) { grades, gradeTemplateInfo ->
            Result.Success(GradesUiState(grades = grades, gradeTemplateInfo = gradeTemplateInfo))
        }
        .stateIn(initialValue = Result.Loading)

    fun onDelete() {
        if (isDeleted.value) {
            return
        }

        viewModelScope.launch {
            repository.deleteGradeTemplateById(gradeTemplateId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val GRADE_TEMPLATE_ID_KEY = GradeNavigation.gradeTemplateIdArg
        private const val IS_DELETED_KEY = "is-deleted"
    }
}