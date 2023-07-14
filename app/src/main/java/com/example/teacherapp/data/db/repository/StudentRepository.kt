package com.example.teacherapp.data.db.repository

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.data.db.datasources.student.StudentDataSource
import com.example.teacherapp.data.models.entities.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val dataSource: StudentDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) {

    fun getStudentByIdOrNull(id: Long): Flow<Result<Student?>> = dataSource
        .getStudentById(id)
        .asResult()

    fun getStudentById(id: Long): Flow<Result<Student>> = dataSource
        .getStudentById(id)
        .asResultNotNull()

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> = dataSource
        .getStudentSchoolClassNameById(schoolClassId)

    suspend fun insertOrUpdateStudent(
        id: Long?,
        schoolClassId: Long,
        orderInClass: Long?,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    ) {
        scope.launch {
            dataSource.insertOrUpdateStudent(
                id = id,
                schoolClassId = schoolClassId,
                orderInClass = orderInClass,
                name = name,
                surname = surname,
                email = email,
                phone = phone,
            )
        }
    }

    suspend fun deleteStudentById(id: Long) {
        scope.launch {
            dataSource.deleteStudentById(id)
        }
    }
}