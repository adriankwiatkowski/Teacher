package com.example.teacher.core.database.datasource.studentnote

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.datasource.utils.querymapper.toExternal
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.model.data.BasicStudentNote
import com.example.teacher.core.model.data.StudentNote
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class StudentNoteDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : StudentNoteDataSource {

    private val studentNoteQueries = db.studentNoteQueries
    private val studentQueries = db.studentQueries

    override fun getStudentNotesByStudentId(studentId: Long): Flow<List<BasicStudentNote>> =
        studentNoteQueries
            .getStudentNotesByStudentId(studentId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getStudentNoteById(id: Long): Flow<StudentNote?> =
        studentNoteQueries
            .getStudentNoteById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getStudentFullNameNameById(studentId: Long): Flow<String?> =
        studentQueries
            .getStudentNameById(studentId)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map { data ->
                if (data == null) {
                    return@map null
                }
                "${data.name} ${data.surname}"
            }

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