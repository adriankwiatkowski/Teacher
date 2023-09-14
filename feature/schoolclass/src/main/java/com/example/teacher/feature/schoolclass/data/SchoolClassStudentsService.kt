package com.example.teacher.feature.schoolclass.data

import com.example.teacher.core.model.data.BasicStudent
import kotlinx.coroutines.flow.StateFlow

interface SchoolClassStudentsService {

    val randomStudent: StateFlow<BasicStudent?>

    fun pickRandomStudent()
}