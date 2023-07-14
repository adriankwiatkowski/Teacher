package com.example.teacherapp.data.db.repository

import android.database.sqlite.SQLiteException
import com.example.teacherapp.data.db.datasources.student.note.StudentNoteDataSource
import com.example.teacherapp.data.di.ApplicationScope
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.data.models.entities.StudentNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class StudentNoteRepository @Inject constructor(
    private val dataSource: StudentNoteDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) {

    fun getStudentNotesByStudentId(studentId: Long): Flow<Resource<List<BasicStudentNote>>> = flow {
        dataSource
            .getStudentNotesByStudentId(studentId)
            .catch { e ->
                if (e !is SQLiteException) {
                    throw e
                }

                emit(Resource.Error(NoSuchElementException()))
            }
            .onStart { emit(Resource.Loading) }
            .collect { data -> emit(Resource.Success(data)) }
    }

    fun getStudentNoteByIdOrNull(id: Long): Flow<Resource<StudentNote?>> = flow {
        if (id == 0L) {
            return@flow emit(Resource.Success(null))
        }

        dataSource
            .getStudentNoteById(id)
            .catch { e ->
                if (e !is SQLiteException) {
                    throw e
                }

                emit(Resource.Error(NoSuchElementException()))
            }
            .onStart { emit(Resource.Loading) }
            .collect { data -> emit(Resource.Success(data)) }
    }

    fun getStudentFullNameNameById(studentId: Long): Flow<String?> =
        dataSource.getStudentFullNameNameById(studentId)

    suspend fun insertOrUpdateStudentNote(
        id: Long?,
        studentId: Long,
        title: String,
        description: String,
        isNegative: Boolean,
    ) {
        scope.launch {
            dataSource.insertOrUpdateStudentNote(
                id = id,
                studentId = studentId,
                title = title,
                description = description,
                isNegative = isNegative,
            )
        }
    }

    suspend fun deleteStudentNoteById(id: Long) {
        scope.launch {
            dataSource.deleteStudentNoteById(id)
        }
    }
}