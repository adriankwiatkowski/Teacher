package com.example.teacherapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.teacherapp.data.models.FabAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherAppViewModel @Inject constructor() : ViewModel() {

    var title by mutableStateOf("")

    private val fabActions = mutableStateListOf<FabAction>()
    val fabAction: FabAction? get() = fabActions.firstOrNull()

    fun addFabAction(fabAction: FabAction) {
        fabActions.add(fabAction)
    }

    fun removeFabAction(fabAction: FabAction) {
        fabActions.remove(fabAction)
    }
}