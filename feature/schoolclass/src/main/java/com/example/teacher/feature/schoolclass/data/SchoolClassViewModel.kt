package com.example.teacher.feature.schoolclass.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SchoolClassViewModel @Inject constructor(
    private val repository: SchoolClassRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), SchoolClassStudentsService {

    private val schoolClassId = savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)
    private val randomStudentNumber = savedStateHandle.getStateFlow(RANDOM_STUDENT_NUMBER, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassResult: StateFlow<Result<SchoolClass>> = schoolClassId
        .flatMapLatest { schoolClassId -> repository.getSchoolClassById(schoolClassId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    override val randomStudent: StateFlow<BasicStudent?> =
        combine(schoolClassResult, randomStudentNumber) { schoolClassResult, registerNumber ->
            val schoolClass = (schoolClassResult as? Result.Success)?.data ?: return@combine null
            schoolClass.students.firstOrNull { student -> student.registerNumber == registerNumber }
        }.stateIn(initialValue = null)

    override fun pickRandomStudent() {
        val schoolClass = (schoolClassResult.value as? Result.Success)?.data ?: return
        val students = schoolClass.students
        if (students.isEmpty()) {
            return
        }

        savedStateHandle[RANDOM_STUDENT_NUMBER] = students.random().registerNumber
    }

    fun onDelete() {
        if (isDeleted.value) {
            return
        }

        viewModelScope.launch {
            repository.deleteSchoolClassById(schoolClassId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    private fun <T> Flow<T>.stateIn(initialValue: T): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = initialValue,
    )

    companion object {
        private const val SCHOOL_CLASS_ID_KEY = SchoolClassNavigation.schoolClassIdArg
        private const val IS_DELETED_KEY = "is-deleted"
        private const val RANDOM_STUDENT_NUMBER = "random-student-number"
    }
}