package com.example.teacherapp.core.data.repository.studentnote

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicStudentNote
import com.example.teacherapp.core.model.data.StudentNote
import kotlinx.coroutines.flow.Flow

interface StudentNoteRepository {

    fun getStudentNotesByStudentId(studentId: Long): Flow<Result<List<BasicStudentNote>>>

    fun getStudentNoteOrNullById(id: Long): Flow<Result<StudentNote?>>

    fun getStudentFullNameNameById(studentId: Long): Flow<String?>

    suspend fun insertOrUpdateStudentNote(
        id: Long?,
        studentId: Long,
        title: String,
        description: String,
        isNegative: Boolean,
    )

    suspend fun deleteStudentNoteById(id: Long)
}