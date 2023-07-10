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

    private val studentNoteQueries = db.studentNoteQueries
    private val studentQueries = db.studentQueries

    override fun getStudentNotesByStudentId(studentId: Long): Flow<List<BasicStudentNote>> {
        return studentNoteQueries
            .getStudentNotesByStudentId(studentId)
            .asFlow()
            .mapToList()
            .map { list ->
                list.map { data -> data.mapToBasicNote() }
            }
    }

    override fun getStudentNoteById(id: Long): Flow<StudentNote?> {
        return studentNoteQueries
            .getStudentNoteById(id)
            .asFlow()
            .mapToOneOrNull()
            .map { data -> data.mapToNote() }
    }

    override fun getStudentFullNameNameById(studentId: Long): Flow<String?> {
        return studentQueries.getStudentNameById(studentId)
            .asFlow()
            .mapToOneOrNull()
            .map { data ->
                if (data == null) {
                    return@map null
                }
                "${data.name} ${data.surname}"
            }
    }

    override suspend fun insertOrUpdateStudentNote(
        id: Long?,
        studentId: Long,
        title: String,
        description: String,
        isNegative: Boolean
    ): Unit = withContext(dispatchers.io) {
        if (id == null) {
            studentNoteQueries.insertStudentNote(
                id = null,
                student_id = studentId,
                title = title,
                description = description,
                is_negative = isNegative,
            )
        } else {
            studentNoteQueries.updateStudentNote(
                id = id,
                student_id = studentId,
                title = title,
                description = description,
                is_negative = isNegative,
            )
        }
    }

    override suspend fun deleteStudentNoteById(id: Long): Unit = withContext(dispatchers.io) {
        studentNoteQueries.deleteStudentNoteById(id)
    }
}