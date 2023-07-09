package com.example.teacherapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.teacherapp.data.models.ActionMenuItem
import com.example.teacherapp.data.models.FabAction
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {

    var title by mutableStateOf("")

    private val _actionMenuItems = mutableStateListOf<ActionMenuItem>()
    val actionMenuItems: List<ActionMenuItem> = _actionMenuItems

    private val fabActions = mutableStateListOf<FabAction>()
    val fabAction: FabAction? get() = fabActions.firstOrNull()

    fun addActionMenuItems(actionMenuItems: List<ActionMenuItem>) {
        this._actionMenuItems.addAll(actionMenuItems)
    }

    fun removeActionMenuItems(actionMenuItems: List<ActionMenuItem>) {
        this._actionMenuItems.removeAll(actionMenuItems.toSet())
    }

    fun addFabAction(fabAction: FabAction) {
        fabActions.add(fabAction)
    }

    fun removeFabAction(fabAction: FabAction) {
        fabActions.remove(fabAction)
    }
}