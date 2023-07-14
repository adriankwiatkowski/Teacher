package com.example.teacherapp.core.data.repository.student

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.database.datasource.student.StudentDataSource
import com.example.teacherapp.core.model.data.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DatabaseStudentRepository @Inject constructor(
    private val dataSource: StudentDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : StudentRepository {

    override fun getStudentByIdOrNull(id: Long): Flow<Result<Student?>> = dataSource
        .getStudentById(id)
        .asResult()

    override fun getStudentById(id: Long): Flow<Result<Student>> = dataSource
        .getStudentById(id)
        .asResultNotNull()

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> = dataSource
        .getStudentSchoolClassNameById(schoolClassId)

    override suspend fun insertOrUpdateStudent(
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

    override suspend fun deleteStudentById(id: Long) {
        scope.launch {
            dataSource.deleteStudentById(id)
        }
    }
}