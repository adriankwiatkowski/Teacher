package com.example.teacherapp.data.models

sealed class ResourceStatus {
    object Loading : ResourceStatus()
    object Success : ResourceStatus()
    object Error: ResourceStatus()
}