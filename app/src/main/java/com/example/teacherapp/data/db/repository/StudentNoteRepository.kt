package com.example.teacherapp.data.db.repository

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.database.datasource.studentnote.StudentNoteDataSource
import com.example.teacherapp.core.model.data.BasicStudentNote
import com.example.teacherapp.core.model.data.StudentNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class StudentNoteRepository @Inject constructor(
    private val dataSource: StudentNoteDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) {

    fun getStudentNotesByStudentId(studentId: Long): Flow<Result<List<BasicStudentNote>>> =
        dataSource
            .getStudentNotesByStudentId(studentId)
            .asResult()

    fun getStudentNoteByIdOrNull(id: Long): Flow<Result<StudentNote?>> = dataSource
        .getStudentNoteById(id)
        .asResultNotNull()

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