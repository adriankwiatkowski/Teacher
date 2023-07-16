package com.example.teacherapp.ui.screens.grade.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.grade.GradeRepository
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradesViewModel @Inject constructor(
    private val repository: GradeRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val gradeTemplateId = savedStateHandle.getStateFlow(GRADE_TEMPLATE_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_GRADE_TEMPLATE_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val gradesResult: StateFlow<Result<List<BasicGradeForTemplate>>> = gradeTemplateId
        .flatMapLatest { gradeTemplateId -> repository.getGradesByGradeTemplateId(gradeTemplateId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    fun onDeleteStudent() {
        if (isDeleted.value) {
            return
        }

        viewModelScope.launch {
//            repository.deleteStudentById(gradeTemplateId.value)
            savedStateHandle[IS_GRADE_TEMPLATE_DELETED_KEY] = true
        }
    }

    companion object {
        private const val GRADE_TEMPLATE_ID_KEY = LessonNavigation.gradeTemplateIdArg
        private const val IS_GRADE_TEMPLATE_DELETED_KEY = "is-grade-template-deleted"
    }
}