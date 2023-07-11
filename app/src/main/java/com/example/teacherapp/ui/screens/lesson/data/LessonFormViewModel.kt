package com.example.teacherapp.ui.screens.lesson.data

import android.database.sqlite.SQLiteException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.lesson.LessonDataSource
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.ResourceStatus
import com.example.teacherapp.data.models.entities.Lesson
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.ui.nav.TeacherDestinationsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonFormViewModel @Inject constructor(
    private val lessonDataSource: LessonDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val lessonId: StateFlow<Long> = savedStateHandle.getStateFlow(LESSON_ID_KEY, 0L)
    private val schoolClassId: StateFlow<Long> =
        savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)

    private val resourceStatus = MutableStateFlow<ResourceStatus>(ResourceStatus.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val lesson: Flow<Lesson?> = lessonId
        .flatMapLatest { id ->
            resourceStatus.value = ResourceStatus.Loading
            val result = lessonDataSource.getLessonById(id)
            resourceStatus.value = ResourceStatus.Success
            result
        }
        .catch { e ->
            if (e !is SQLiteException) {
                throw e
            }

            resourceStatus.value = ResourceStatus.Error
        }

    var form by mutableStateOf(LessonFormProvider.createDefaultForm())
        private set

    val lessonResource: StateFlow<Resource<Lesson?>> =
        combine(resourceStatus, lesson) { status, lesson ->
            when (status) {
                ResourceStatus.Loading -> Resource.Loading
                ResourceStatus.Error -> Resource.Error(NoSuchElementException())
                ResourceStatus.Success -> Resource.Success(lesson)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Resource.Loading,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassName: StateFlow<String?> = schoolClassId.flatMapLatest { schoolClassId ->
        lessonDataSource.getStudentSchoolClassNameById(schoolClassId = schoolClassId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = null
    )

    init {
        lesson
            .onEach { lesson ->
                if (lesson == null) {
                    return@onEach
                }

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
        if (!form.isValid) {
            return
        }

        val handler = CoroutineExceptionHandler { _, exception ->
            form = form.copy(status = FormStatus.Error)
            if (exception !is SQLiteException) {
                throw exception
            }
        }
        viewModelScope.launch(handler) {
            if (form.status == FormStatus.Saving || form.status == FormStatus.Success) {
                return@launch
            }

            val id = form.id
            val name = form.name.value.trim()

            form = form.copy(status = FormStatus.Saving)

            lessonDataSource.insertOrUpdateLesson(
                id = id,
                schoolClassId = schoolClassId.value,
                name = name,
            )

            form = form.copy(status = FormStatus.Success)
        }
    }

    companion object {
        private const val LESSON_ID_KEY = TeacherDestinationsArgs.LESSON_ID_ARG
        private const val SCHOOL_CLASS_ID_KEY = TeacherDestinationsArgs.SCHOOL_CLASS_ID_ARG
    }
}