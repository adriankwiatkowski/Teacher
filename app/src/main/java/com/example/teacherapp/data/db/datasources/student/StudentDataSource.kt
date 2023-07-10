package com.example.teacherapp.data.db.datasources.student

import com.example.teacherapp.data.models.entities.BasicStudent
import com.example.teacherapp.data.models.entities.Student
import kotlinx.coroutines.flow.Flow

interface StudentDataSource {

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