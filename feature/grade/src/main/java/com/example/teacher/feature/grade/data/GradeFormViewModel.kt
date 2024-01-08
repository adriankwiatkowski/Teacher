package com.example.teacher.feature.grade.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.combineResult
import com.example.teacher.core.data.repository.grade.GradeRepository
import com.example.teacher.core.data.repository.gradescore.GradeScoreRepository
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.Grade
import com.example.teacher.core.model.data.GradeScore
import com.example.teacher.core.model.data.GradeTemplateInfo
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.grade.nav.GradeNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
internal class GradeFormViewModel @Inject constructor(
    private val repository: GradeRepository,
    private val gradeScoreRepository: GradeScoreRepository,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val initialGrade: StateFlow<BigDecimal?> = gradeResult
        .mapLatest { gradeResult ->
            (gradeResult as? Result.Success)?.data?.grade
        }
        .stateIn(initialValue = null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val gradeScoreResult: StateFlow<Result<GradeScore?>> = gradeTemplateId
        .flatMapLatest { gradeTemplateId ->
            gradeScoreRepository.getGradeScoreByGradeTemplateId(gradeTemplateId)
        }
        .stateIn(initialValue = Result.Loading)

    private val _gradeScoreData = MutableStateFlow(GradeScoreDataProvider.createDefault())
    val gradeScoreData = _gradeScoreData.asStateFlow()

    private val _form = MutableStateFlow(GradeFormProvider.createDefaultForm())
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    init {
        gradeResult
            .onEach { gradeResult ->
                val grade = (gradeResult as? Result.Success)?.data
                if (grade == null) {
                    _form.value = GradeFormProvider.createDefaultForm(status = FormStatus.Idle)
                    initialForm.value = form.value
                    return@onEach
                }

                _form.value = form.value.copy(
                    grade = GradeFormProvider.validateGrade(grade.grade),
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)

        gradeScoreResult
            .onEach { gradeScoreResult ->
                val gradeScore = (gradeScoreResult as? Result.Success)?.data
                setGradeScoreDataThresholds(gradeScore)
            }
            .launchIn(viewModelScope)
    }

    fun onGradeChange(grade: BigDecimal?) {
        _form.value = form.value.copy(grade = GradeFormProvider.validateGrade(grade))
    }

    fun onGradeScoreThresholdChange(grade: BigDecimal, newMinThreshold: Int) {
        _gradeScoreData.value = GradeScoreDataProvider.validateGradeScores(
            gradeScoreData = gradeScoreData.value,
            grade = grade,
            newMinThreshold = newMinThreshold,
        )
    }

    fun onMaxScoreChange(maxScore: String?) {
        _gradeScoreData.value = gradeScoreData.value.copy(
            maxScore = GradeScoreDataProvider.validateGradeScore(maxScore)
        )
        _gradeScoreData.value = GradeScoreDataProvider.calculateGrade(gradeScoreData.value)
    }

    fun onStudentScoreChange(studentScore: String?) {
        _gradeScoreData.value = gradeScoreData.value.copy(
            studentScore = GradeScoreDataProvider.validateGradeScore(studentScore)
        )
        _gradeScoreData.value = GradeScoreDataProvider.calculateGrade(gradeScoreData.value)
    }

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.upsertGrade(
                id = gradeId.value.let { id -> if (id != 0L) id else null },
                studentId = studentId.value,
                gradeTemplateId = gradeTemplateId.value,
                grade = form.grade.value!!,
            )

            if (isActive) {
                _form.value = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onSaveGradeScore() {
        viewModelScope.launch {
            val gradeScore = (gradeScoreResult.value as? Result.Success)?.data ?: return@launch
            val updatedGradeScore = gradeScoreData.value.toGradeScore(
                id = gradeScore.id,
                gradeTemplateId = gradeTemplateId.value,
            )
            gradeScoreRepository.updateGradeScore(updatedGradeScore)
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            repository.deleteGradeById(gradeId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    private fun setGradeScoreDataThresholds(gradeScore: GradeScore?) {
        _gradeScoreData.update { gradeScoreData ->
            val gradeScoreThresholds = GradeScoreDataProvider.getGradeScoreThresholds(gradeScore)
            val updated = gradeScoreData.copy(gradeScoreThresholds = gradeScoreThresholds)
            GradeScoreDataProvider.calculateGrade(updated)
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val GRADE_TEMPLATE_ID_KEY = GradeNavigation.gradeTemplateIdArg
        private const val STUDENT_ID_KEY = GradeNavigation.studentIdArg
        private const val GRADE_ID_KEY = GradeNavigation.gradeIdArg
        private const val IS_DELETED_KEY = "is-deleted"
    }
}