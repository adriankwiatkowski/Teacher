package com.example.teacher.feature.schoolclass.data

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.feature.schoolclass.nav.SchoolClassNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SchoolClassScaffoldViewModel @Inject constructor(
    private val repository: SchoolClassRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val schoolClassId = savedStateHandle.getStateFlow(SCHOOL_CLASS_ID_KEY, 0L)
    val isDeleted = savedStateHandle.getStateFlow(IS_DELETED_KEY, false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val schoolClassResult: StateFlow<Result<SchoolClass>> = schoolClassId
        .flatMapLatest { schoolClassId -> repository.getSchoolClassById(schoolClassId) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Result.Loading,
        )

    fun onDelete() {
        if (isDeleted.value) {
            return
        }

        viewModelScope.launch {
            repository.deleteSchoolClassById(schoolClassId.value)
            savedStateHandle[IS_DELETED_KEY] = true
        }
    }

    companion object {
        private const val SCHOOL_CLASS_ID_KEY = SchoolClassNavigation.schoolClassIdArg
        private const val IS_DELETED_KEY = "is-deleted"
    }
}