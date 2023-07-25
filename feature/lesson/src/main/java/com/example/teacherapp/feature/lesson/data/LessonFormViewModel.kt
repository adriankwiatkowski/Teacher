package com.example.teacherapp.feature.lesson.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.lesson.LessonRepository
import com.example.teacherapp.core.model.data.Lesson
import com.example.teacherapp.core.ui.model.FormStatus
import com.example.teacherapp.feature.lesson.LessonNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

    var form by mutableStateOf(LessonFormProvider.createDefaultForm())
        private set

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
                form = form.copy(
                    id = lesson.id,
                    name = LessonFormProvider.validateName(lesson.name),
                    status = if (form.status is FormStatus.Success) form.status else FormStatus.Idle,
                )
            }
            .launchIn(viewModelScope)
    }

    fun onNameChange(name: String) {
        form = form.copy(name = LessonFormProvider.validateName(name))
    }

    fun onSubmit() {
        if (!form.isSubmitEnabled) {
            return
        }

        form = form.copy(status = FormStatus.Saving)
        viewModelScope.launch {
            val saved = lessonRepository.insertOrUpdateLesson(
                id = form.id,
                schoolClassId = schoolClassId.value,
                name = form.name.value.trim(),
            )

            if (isActive) {
                form = form.copy(status = if (saved) FormStatus.Success else FormStatus.Error)
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