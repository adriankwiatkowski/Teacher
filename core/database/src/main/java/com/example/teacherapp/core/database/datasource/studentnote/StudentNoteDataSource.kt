package com.example.teacherapp.core.database.datasource.studentnote

import com.example.teacherapp.core.model.data.BasicStudentNote
import com.example.teacherapp.core.model.data.StudentNote
import kotlinx.coroutines.flow.Flow

interface StudentNoteDataSource {

    fun getStudentNotesByStudentId(studentId: Long): Flow<List<BasicStudentNote>>

    fun getStudentNoteById(id: Long): Flow<StudentNote?>

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