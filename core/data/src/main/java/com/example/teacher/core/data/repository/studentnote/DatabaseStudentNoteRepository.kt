package com.example.teacher.core.data.repository.studentnote

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.database.datasource.studentnote.StudentNoteDataSource
import com.example.teacher.core.model.data.BasicStudentNote
import com.example.teacher.core.model.data.StudentNote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseStudentNoteRepository @Inject constructor(
    private val dataSource: StudentNoteDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : StudentNoteRepository {

    override fun getStudentNotesByStudentId(studentId: Long): Flow<Result<List<BasicStudentNote>>> =
        dataSource
            .getStudentNotesByStudentId(studentId)
            .asResult()

    override fun getStudentNoteOrNullById(id: Long): Flow<Result<StudentNote?>> = dataSource
        .getStudentNoteById(id)
        .asResult()

    override fun getStudentFullNameNameById(studentId: Long): Flow<String?> =
        dataSource.getStudentFullNameNameById(studentId)

    override suspend fun insertOrUpdateStudentNote(
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

    override suspend fun deleteStudentNoteById(id: Long) {
        scope.launch {
            dataSource.deleteStudentNoteById(id)
        }
    }
}