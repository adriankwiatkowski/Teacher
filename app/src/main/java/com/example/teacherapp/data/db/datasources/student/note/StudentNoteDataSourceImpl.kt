package com.example.teacherapp.data.db.datasources.student.note

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.utils.querymappers.StudentNoteMapper.mapToBasicNote
import com.example.teacherapp.data.db.datasources.utils.querymappers.StudentNoteMapper.mapToNote
import com.example.teacherapp.data.di.DefaultDispatcher
import com.example.teacherapp.data.models.entities.BasicStudentNote
import com.example.teacherapp.data.models.entities.StudentNote
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StudentNoteDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : StudentNoteDataSource {

    private val studentNoteQueries = db.studentNoteQueries
    private val studentQueries = db.studentQueries

    override fun getStudentNotesByStudentId(studentId: Long): Flow<List<BasicStudentNote>> =
        studentNoteQueries
            .getStudentNotesByStudentId(studentId)
            .asFlow()
            .mapToList()
            .map { list ->
                list.map { data -> data.mapToBasicNote() }
            }

    override fun getStudentNoteById(id: Long): Flow<StudentNote?> =
        studentNoteQueries
            .getStudentNoteById(id)
            .asFlow()
            .mapToOneOrNull()
            .map { data -> data.mapToNote() }

    override fun getStudentFullNameNameById(studentId: Long): Flow<String?> =
        studentQueries.getStudentNameById(studentId)
            .asFlow()
            .mapToOneOrNull()
            .map { data ->
                if (data == null) {
                    return@map null
                }
                "${data.name} ${data.surname}"
            }

    // TODO: This and other methods should use Application Scope, preferably use Repository.
    override suspend fun insertOrUpdateStudentNote(
        id: Long?,
        studentId: Long,
        title: String,
        description: String,
        isNegative: Boolean,
    ): Unit = withContext(dispatcher) {
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

    override suspend fun deleteStudentNoteById(id: Long): Unit = withContext(dispatcher) {
        studentNoteQueries.deleteStudentNoteById(id)
    }
}