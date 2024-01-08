package com.example.teacher.core.database.datasource.studentnote

import com.example.teacher.core.model.data.BasicStudentNote
import com.example.teacher.core.model.data.StudentNote
import kotlinx.coroutines.flow.Flow

interface StudentNoteDataSource {

    fun getStudentNotesByStudentId(studentId: Long): Flow<List<BasicStudentNote>>

    fun getStudentNoteById(id: Long): Flow<StudentNote?>

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