package com.example.teacherapp.core.data.repository.student

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.Student
import kotlinx.coroutines.flow.Flow

interface StudentRepository {

    fun getStudentOrNullById(id: Long): Flow<Result<Student?>>

    fun getStudentById(id: Long): Flow<Result<Student>>

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