package com.example.teacherapp.ui.screens.student.note.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.repository.StudentNoteRepository
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StudentNotesViewModel @Inject constructor(
    private val repository: StudentNoteRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val studentNotesResource: StateFlow<Resource<List<BasicStudentNote>>> = studentId
        .flatMapLatest { studentId -> repository.getStudentNotesByStudentId(studentId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Resource.Loading,
        )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
    }
}