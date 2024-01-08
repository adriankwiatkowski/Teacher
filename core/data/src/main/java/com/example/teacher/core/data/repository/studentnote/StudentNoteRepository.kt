package com.example.teacher.core.data.repository.studentnote

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicStudentNote
import com.example.teacher.core.model.data.StudentNote
import kotlinx.coroutines.flow.Flow

interface StudentNoteRepository {

    fun getStudentNotesByStudentId(studentId: Long): Flow<Result<List<BasicStudentNote>>>

    fun getStudentNoteOrNullById(id: Long): Flow<Result<StudentNote?>>

    fun getStudentFullNameNameById(studentId: Long): Flow<String?>

    suspend fun upsertStudentNote(
        id: Long?,
        studentId: Long,
        title: String,
        description: String,
        isNegative: Boolean,
    )

    suspend fun deleteStudentNoteById(id: Long)
}