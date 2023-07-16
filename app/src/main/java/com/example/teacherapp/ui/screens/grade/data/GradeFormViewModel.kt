package com.example.teacherapp.ui.screens.grade.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.combineResult
import com.example.teacherapp.core.data.repository.grade.GradeRepository
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Grade
import com.example.teacherapp.core.model.data.GradeTemplateInfo
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class GradeFormViewModel @Inject constructor(
    private val repository: GradeRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val gradeTemplateId = savedStateHandle.getStateFlow(GRADE_TEMPLATE_ID_KEY, 0L)
    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)
    private val gradeId = savedStateHandle.getStateFlow(GRADE_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val gradeTemplateInfoResult: StateFlow<Result<GradeTemplateInfo>> = gradeTemplateId
        .flatMapLatest { gradeTemplateId ->
            repository.getGradeTemplateInfoByGradeTemplateId(gradeTemplateId)
        }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val studentResult: StateFlow<Result<BasicStudent>> = studentId
        .flatMapLatest { studentId -> repository.getStudentById(studentId) }
        .stateIn(initialValue = Result.Loading)

    val uiState: StateFlow<Result<GradeFormUiState>> = gradeTemplateInfoResult
        .combineResult(studentResult) { gradeTemplateInfo, student ->
            Result.Success(
                GradeFormUiState(
                    gradeTemplateInfo = gradeTemplateInfo,
                    student = student,
                )
            )
        }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val gradeResult: StateFlow<Result<Grade?>> = gradeId
        .flatMapLatest { gradeId -> repository.getGradeOrNullById(gradeId) }
        .stateIn(initialValue = Result.Loading)

    var form by mutableStateOf(GradeFormProvider.createDefaultForm())
        private set

    init {
        gradeResult
            .onEach { gradeResult ->
                val grade = (gradeResult as? Result.Success)?.data
                if (grade == null) {
                    form = GradeFormProvider.createDefaultForm(status = FormStatus.Idle)
                    return@onEach
                }

                form = form.copy(
                    grade = GradeFormProvider.validateGrade(grade.grade),
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onGradeChange(grade: BigDecimal?) {
        form = form.copy(grade = GradeFormProvider.validateGrade(grade))
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateGrade(
                id = gradeId.value.let { id -> if (id != 0L) id else null },
                studentId = studentId.value,
                gradeTemplateId = gradeTemplateId.value,
                grade = form.grade.value!!,
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            repository.deleteGradeById(gradeId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val GRADE_TEMPLATE_ID_KEY = LessonNavigation.gradeTemplateIdArg
        private const val STUDENT_ID_KEY = LessonNavigation.studentIdArg
        private const val GRADE_ID_KEY = LessonNavigation.gradeIdArg
        private const val IS_DELETED_KEY = "is-deleted"
    }
}