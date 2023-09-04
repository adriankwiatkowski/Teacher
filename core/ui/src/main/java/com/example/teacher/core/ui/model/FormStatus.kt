package com.example.teacher.core.ui.model

sealed class FormStatus {
    object Idle : FormStatus()
    object Saving : FormStatus()
    object Success : FormStatus()
    object Error : FormStatus()
}