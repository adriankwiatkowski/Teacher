package com.example.teacherapp.data.models.input

sealed class FormStatus {
    object Idle : FormStatus()
    object Saving : FormStatus()
    object Success : FormStatus()
    object Error: FormStatus()
}