package com.example.teacherapp.ui.screens.student.note.data

import android.database.sqlite.SQLiteException
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacherapp.data.db.datasources.student.note.StudentNoteDataSource
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.ResourceStatus
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.ui.nav.graphs.student.StudentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StudentNotesViewModel @Inject constructor(
    private val studentNoteDataSource: StudentNoteDataSource,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val resourceStatus = MutableStateFlow<ResourceStatus>(ResourceStatus.Loading)
    private val studentId = savedStateHandle.getStateFlow(STUDENT_ID_KEY, 0L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val studentNotes: Flow<List<BasicStudentNote>> = studentId
        .flatMapLatest { id ->
            resourceStatus.value = ResourceStatus.Loading
            studentNoteDataSource.getStudentNotesByStudentId(id).also {
                resourceStatus.value = ResourceStatus.Success
            }
        }
        .catch { e ->
            if (e !is SQLiteException) {
                throw e
            }

            resourceStatus.value = ResourceStatus.Error
        }

    val studentNotesResource: StateFlow<Resource<List<BasicStudentNote>>> =
        combine(resourceStatus, studentNotes) { status, studentNotes ->
            when (status) {
                ResourceStatus.Loading -> Resource.Loading
                ResourceStatus.Error -> Resource.Error(NoSuchElementException())
                ResourceStatus.Success -> Resource.Success(studentNotes)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Resource.Loading,
        )

    companion object {
        private const val STUDENT_ID_KEY = StudentNavigation.studentIdArg
    }
}