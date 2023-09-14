package com.example.teacher.feature.student.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.mapResult
import com.example.teacher.core.data.repository.studentnote.StudentNoteRepository
import com.example.teacher.feature.student.nav.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class StudentNotesViewModel @Inject constructor(
    private val repository: StudentNoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentNotesResult: StateFlow<Result<StudentNotes>> = studentId
        .flatMapLatest { studentId -> repository.getStudentNotesByStudentId(studentId) }
        .mapResult { notes ->
            Result.Success(
                StudentNotes(
                    neutralNotes = notes.filter { note -> !note.isNegative },
                    negativeNotes = notes.filter { note -> note.isNegative },
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
    }
}