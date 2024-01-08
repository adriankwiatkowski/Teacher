package com.example.teacher.feature.lesson.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.lesson.LessonRepository
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
internal class LessonFormViewModel @Inject constructor(
    private val lessonRepository: LessonRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId: StateFlow<Long> = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)
    private val schoolClassId: StateFlow<Long> =
        savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)

    private val _form = MutableStateFlow(LessonFormProvider.createDefaultForm())
    val form = _form.asStateFlow()

    private val initialForm = MutableStateFlow(form.value)
    val isFormMutated = combine(form, initialForm) { form, initialForm -> form != initialForm }
        .stateIn(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val lessonResult: StateFlow<Result<Lesson?>> = lessonId
        .flatMapLatest { lessonId -> lessonRepository.getLessonOrNullById(lessonId) }
        .stateIn(initialValue = Result.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassName: StateFlow<String?> = schoolClassId
        .flatMapLatest { schoolClassId -> lessonRepository.getSchoolClassNameById(schoolClassId) }
        .stateIn(initialValue = null)

    init {
        lessonResult
            .onEach { lessonResource ->
                val lesson = (lessonResource as? Result.Success)?.data ?: return@onEach
                _form.value = form.value.copy(
                    id = lesson.id,
                    name = LessonFormProvider.validateName(lesson.name),
                    status = if (form.value.status is FormStatus.Success) form.value.status else FormStatus.Idle,
                )
                initialForm.value = form.value
            }
            .launchIn(viewModelScope)
    }

    fun onNameChange(name: String) {
        _form.value = form.value.copy(name = LessonFormProvider.validateName(name))
    }

    fun onSubmit() {
        val form = form.value

        if (!form.isSubmitEnabled) {
            return
        }

        _form.value = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            val saved = lessonRepository.upsertLesson(
                id = form.id,
                schoolClassId = schoolClassId.value,
                name = form.name.value.trim(),
            )

            if (isActive) {
                _form.value =
                    form.copy(status = if (saved) FormStatus.Success else FormStatus.Error)
            }
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val LESSON_ID_KEY = LessonNavigation.lessonIdArg
        private const val SCHOOL_CLASS_ID_KEY = LessonNavigation.schoolClassIdArg
    }
}