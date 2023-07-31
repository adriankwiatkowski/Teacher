package com.example.teacherapp.feature.student.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.data.repository.studentnote.StudentNoteRepository
import com.example.teacherapp.core.model.data.BasicStudentNote
import com.example.teacherapp.feature.student.nav.StudentNavigation
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
    val studentNotesResult: StateFlow<Result<List<BasicStudentNote>>> = studentId
        .flatMapLatest { studentId -> repository.getStudentNotesByStudentId(studentId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
    }
}