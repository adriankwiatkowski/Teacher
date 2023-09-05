package com.example.teacher.core.ui.model

sealed class FormStatus {
    data object Idle : FormStatus()
    data object Saving : FormStatus()
    data object Success : FormStatus()
    data object Error : FormStatus()
}