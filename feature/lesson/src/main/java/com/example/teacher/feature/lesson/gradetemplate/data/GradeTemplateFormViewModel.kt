package com.example.teacher.feature.lesson.gradetemplate.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.gradetemplate.GradeTemplateRepository
import com.example.teacher.core.model.data.GradeTemplate
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.feature.lesson.nav.LessonNavigation
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GradeTemplateFormViewModel @Inject constructor(
    private val repository: GradeTemplateRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)
    private val gradeTemplateId = savedStateHandle.getStateFlow(GRADE_TEMPLATE_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val gradeTemplateResult: StateFlow<Result<GradeTemplate?>> = gradeTemplateId
        .flatMapLatest { gradeTemplateId -> repository.getGradeTemplateOrNullById(gradeTemplateId) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonResult: StateFlow<Result<Lesson>> = lessonId
        .flatMapLatest { lessonId -> repository.getLessonById(lessonId) }
        .stateIn(initialValue = Result.Loading)

    private val _form = MutableStateFlow(GradeTemplateFormProvider.createDefaultForm())
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    init {
        gradeTemplateResult
            .onEach { gradeTemplateResult ->
                val grade = (gradeTemplateResult as? Result.Success)?.data
                if (grade == null) {
                    _form.value =
                        GradeTemplateFormProvider.createDefaultForm(status = FormStatus.Idle)
                    initialForm.value = form.value
                    return@onEach
                }

                _form.value = form.value.copy(
                    name = GradeTemplateFormProvider.validateName(grade.name),
                    description = GradeTemplateFormProvider.validateDescription(grade.description),
                    weight = GradeTemplateFormProvider.validateWeight(grade.weight.toString()),
                    isFirstTerm = grade.isFirstTerm,
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onNameChange(name: String) {
        _form.value = form.value.copy(name = GradeTemplateFormProvider.validateName(name))
    }

    fun onDescriptionChange(description: String) {
        _form.value =
            form.value.copy(description = GradeTemplateFormProvider.validateDescription(description))
    }

    fun onWeightChange(weight: String) {
        _form.value = form.value.copy(weight = GradeTemplateFormProvider.validateWeight(weight))
    }

    fun onIsFirstTermChange(isFirstTerm: Boolean) {
        _form.value = form.value.copy(isFirstTerm = isFirstTerm)
    }

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.upsertGradeTemplate(
                id = gradeTemplateId.value.let { id -> if (id != 0L) id else null },
                lessonId = lessonId.value,
                name = form.name.value.trim(),
                description = form.description.value?.trim().orEmpty(),
                weight = form.weight.value.trim().toInt(),
                isFirstTerm = form.isFirstTerm,
            )

            if (isActive) {
                _form.value = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDelete() {
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
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
        private const val GRADE_TEMPLATE_ID_KEY = LessonNavigation.gradeTemplateIdArg
        private const val IS_DELETED_KEY = "is-deleted"
    }
}