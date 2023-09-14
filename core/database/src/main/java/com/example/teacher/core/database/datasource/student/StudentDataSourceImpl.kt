package com.example.teacher.core.database.datasource.student

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.datasource.utils.querymapper.toExternal
import com.example.teacher.core.database.datasource.utils.querymapper.toExternalStudentGrades
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGradesByLesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class StudentDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : StudentDataSource {

    private val queries = db.studentQueries
    private val schoolClassQueries = db.schoolClassQueries

    override fun getBasicStudentById(id: Long): Flow<BasicStudent?> =
        queries
            .getBasicStudentById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getStudentById(id: Long): Flow<Student?> =
        queries
            .getStudentById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getStudentsBySchoolClassId(schoolClassId: Long): Flow<List<BasicStudent>> =
        queries
            .getStudentsBySchoolClassId(schoolClassId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> =
        schoolClassQueries.getSchoolClassNameById(schoolClassId)
            .asFlow()
            .mapToOneOrNull(dispatcher)

    override fun getStudentGradesById(studentId: Long): Flow<List<StudentGradesByLesson>> =
        queries
            .getStudentGradesById(studentId)
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternalStudentGrades)
            .flowOn(dispatcher)

    override suspend fun insertOrUpdateStudent(
        id: Long?,
        schoolClassId: Long,
        registerNumber: Long?,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    ): Unit = withContext(dispatcher) {
        val defaultRegisterNumber = 1L // TODO: Register number should be unique.

        @Suppress("NAME_SHADOWING") val email = if (email.isNullOrBlank()) null else email
        @Suppress("NAME_SHADOWING") val phone = if (phone.isNullOrBlank()) null else phone

        if (id == null) {
            queries.insertStudent(
                id = null,
                school_class_id = schoolClassId,
                register_number = registerNumber ?: defaultRegisterNumber,
                name = name,
                surname = surname,
                email = email,
                phone = phone,
            )
        } else {
            queries.updateStudent(
                name = name,
                surname = surname,
                email = email,
                phone = phone,
                register_number = registerNumber ?: defaultRegisterNumber,
                id = id,
            )
        }
    }

    override suspend fun deleteStudentById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteStudentById(id)
    }
}