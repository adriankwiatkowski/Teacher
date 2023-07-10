package com.example.teacherapp.data.db.datasources.student.note

import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.data.models.entities.StudentNote
import kotlinx.coroutines.flow.Flow

interface StudentNoteDataSource {

    fun getStudentNotesByStudentId(studentId: Long): Flow<List<BasicStudentNote>>

    fun getStudentNoteById(id: Long): Flow<StudentNote?>

    suspend fun insertOrUpdateStudentNote(
        id: Long?,
        studentId: Long,
        title: String,
        description: String,
        isNegative: Boolean,
    )

    suspend fun deleteStudentNoteById(id: Long)
}