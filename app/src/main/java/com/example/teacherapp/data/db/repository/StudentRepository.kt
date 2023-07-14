package com.example.teacherapp.data.db.repository

import android.database.sqlite.SQLiteException
import com.example.teacherapp.data.db.datasources.student.StudentDataSource
import com.example.teacherapp.data.di.ApplicationScope
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val dataSource: StudentDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) {

    fun getStudentByIdOrNull(id: Long): Flow<Resource<Student?>> = flow {
        if (id == 0L) {
            return@flow emit(Resource.Success(null))
        }

        dataSource
            .getStudentById(id)
            .catch { e ->
                if (e !is SQLiteException) {
                    throw e
                }

                emit(Resource.Error(NoSuchElementException()))
            }
            .onStart { emit(Resource.Loading) }
            .collect { student -> emit(Resource.Success(student)) }
    }

    fun getStudentById(id: Long): Flow<Resource<Student>> = getStudentByIdOrNull(id)
        .map { resource ->
            when (resource) {
                is Resource.Success -> {
                    if (resource.data == null) {
                        Resource.Error(NoSuchElementException())
                    } else {
                        Resource.Success(resource.data)
                    }
                }

                is Resource.Error -> Resource.Error(resource.exception)
                Resource.Loading -> Resource.Loading
            }
        }

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> =
        dataSource.getStudentSchoolClassNameById(schoolClassId)

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