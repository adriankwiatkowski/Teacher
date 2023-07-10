package com.example.teacherapp.data.db.datasources.student.note

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.utils.querymappers.StudentNoteMapper.mapToBasicNote
import com.example.teacherapp.data.db.datasources.utils.querymappers.StudentNoteMapper.mapToNote
import com.example.teacherapp.data.di.DispatcherProvider
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.data.models.entities.StudentNote
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StudentNoteDataSourceImpl(
    db: TeacherDatabase,
    private val dispatchers: DispatcherProvider,
) : StudentNoteDataSource {

    private val queries = db.studentNoteQueries

    override fun getStudentNotesByStudentId(studentId: Long): Flow<List<BasicStudentNote>> {
        return queries
            .getStudentNotesByStudentId(studentId)
            .asFlow()
            .mapToList()
            .map { list ->
                list.map { data -> data.mapToBasicNote() }
            }
    }

    override fun getStudentNoteById(id: Long): Flow<StudentNote?> {
        return queries
            .getStudentNoteById(id)
            .asFlow()
            .mapToOneOrNull()
            .map { data -> data.mapToNote() }
    }

    override suspend fun insertOrUpdateStudentNote(
        id: Long?,
        studentId: Long,
        title: String,
        description: String,
        isNegative: Boolean
    ): Unit = withContext(dispatchers.io) {
        if (id == null) {
            queries.insertStudentNote(
                id = null,
                student_id = studentId,
                title = title,
                description = description,
                is_negative = isNegative,
            )
        } else {
            queries.updateStudentNote(
                id = id,
                title = title,
                description = description,
            )
        }
    }

    override suspend fun deleteStudentNoteById(id: Long): Unit = withContext(dispatchers.io) {
        queries.deleteStudentNoteById(id)
    }
}