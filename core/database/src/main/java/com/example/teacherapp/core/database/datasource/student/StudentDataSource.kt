package com.example.teacherapp.core.database.datasource.student

import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Student
import kotlinx.coroutines.flow.Flow

interface StudentDataSource {

    fun getBasicStudentById(id: Long): Flow<BasicStudent?>

    fun getStudentById(id: Long): Flow<Student?>

    fun getStudentsBySchoolClassId(schoolClassId: Long): Flow<List<BasicStudent>>

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?>

    suspend fun insertOrUpdateStudent(
        id: Long?,
        schoolClassId: Long,
        orderInClass: Long?,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    )

    suspend fun deleteStudentById(id: Long)
}