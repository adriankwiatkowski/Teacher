package com.example.teacherapp.feature.lesson.gradetemplate.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.gradetemplate.GradeTemplateRepository
import com.example.teacherapp.core.model.data.GradeTemplate
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.lesson.nav.LessonNavigation
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

    var form by mutableStateOf(GradeTemplateFormProvider.createDefaultForm())
        private set

    init {
        gradeTemplateResult
            .onEach { gradeTemplateResult ->
                val grade = (gradeTemplateResult as? Result.Success)?.data
                if (grade == null) {
                    form = GradeTemplateFormProvider.createDefaultForm(status = FormStatus.Idle)
                    return@onEach
                }

                form = form.copy(
                    name = GradeTemplateFormProvider.validateName(grade.name),
                    description = GradeTemplateFormProvider.validateDescription(grade.description),
                    weight = GradeTemplateFormProvider.validateWeight(grade.weight.toString()),
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onNameChange(name: String) {
        form = form.copy(name = GradeTemplateFormProvider.validateName(name))
    }

    fun onDescriptionChange(description: String) {
        form = form.copy(description = GradeTemplateFormProvider.validateDescription(description))
    }

    fun onWeightChange(weight: String) {
        form = form.copy(weight = GradeTemplateFormProvider.validateWeight(weight))
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            repository.insertOrUpdateGradeTemplate(
                id = gradeTemplateId.value.let { id -> if (id != 0L) id else null },
                lessonId = lessonId.value,
                name = form.name.value.trim(),
                description = form.description.value?.trim().orEmpty(),
                weight = form.weight.value.trim().toInt(),
            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
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