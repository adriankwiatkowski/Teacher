package com.example.teacherapp.ui.screens.gradetemplate.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.GradeTemplate
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.nav.graphs.lesson.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeTemplateFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)
    private val gradeTemplateId = savedStateHandle.getStateFlow(GRADE_TEMPLATE_ID_KEY, 0L)

    // TODO: Actually fetch from repository.
    @OptIn(ExperimentalCoroutinesApi::class)
    val gradeTemplateResult: StateFlow<Result<GradeTemplate?>> = gradeTemplateId
        .flatMapLatest { gradeTemplateId -> flowOf(Result.Success(null)) }
        .stateIn(initialValue = Result.Loading)

    var form by mutableStateOf(GradeTemplateFormProvider.createDefaultForm())
        private set

    var isDeleted by mutableStateOf(false)

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
            // TODO: Insert.
//            repository.insertOrUpdateGradeTemplate(
//                id = gradeTemplateId.value.let { id -> if (id != 0L) id else null },
//                lessonId = lessonId.value,
//                name = form.name.value.trim(),
//                description = form.description.value?.trim().orEmpty(),
//                weight = form.weight.toInt(),
//            )

            if (isActive) {
                form = form.copy(status = FormStatus.Success)
            }
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            isDeleted = false
            // TODO: Delete.
//            repository.deleteGradleTemplateById(gradeTemplateId.value)
            isDeleted = true
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
    }
}