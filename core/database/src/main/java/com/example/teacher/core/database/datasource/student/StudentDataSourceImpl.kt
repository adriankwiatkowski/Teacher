package com.example.teacher.core.database.datasource.student

import app.cash.sqldelight.TransactionWithoutReturn
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.querymapper.toExternal
import com.example.teacher.core.database.querymapper.toExternalStudentGrades
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

    override fun getBasicStudentById(id: Long): Flow<BasicStudent?> = queries
        .getBasicStudentById(id)
        .asFlow()
        .mapToOneOrNull(dispatcher)
        .map(::toExternal)
        .flowOn(dispatcher)

    override fun getStudentById(id: Long): Flow<Student?> = queries
        .getStudentById(id)
        .asFlow()
        .mapToOneOrNull(dispatcher)
        .map(::toExternal)
        .flowOn(dispatcher)

    override fun getStudentsBySchoolClassId(
        schoolClassId: Long
    ): Flow<List<BasicStudent>> = queries
        .getStudentsBySchoolClassId(schoolClassId)
        .asFlow()
        .mapToList(dispatcher)
        .map(::toExternal)
        .flowOn(dispatcher)

    override fun getUsedRegisterNumbersBySchoolClassId(
        schoolClassId: Long
    ): Flow<List<Long>> = queries.getUsedRegisterNumbersBySchoolClassId(schoolClassId)
        .asFlow()
        .mapToList(dispatcher)

    override fun getStudentSchoolClassNameById(
        schoolClassId: Long
    ): Flow<String?> = schoolClassQueries.getSchoolClassNameById(schoolClassId)
        .asFlow()
        .mapToOneOrNull(dispatcher)

    override fun getStudentGradesById(
        studentId: Long
    ): Flow<List<StudentGradesByLesson>> = queries
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
        queries.transaction {
            val actualEmail = if (email.isNullOrBlank()) null else email
            val actualPhone = if (phone.isNullOrBlank()) null else phone
            val actualRegisterNumber = swapRegisterNumbersIfUsedAndGetAvailableNumber(
                id = id,
                schoolClassId = schoolClassId,
                registerNumber = registerNumber,
            )

            if (id == null) {
                queries.insertStudent(
                    id = null,
                    school_class_id = schoolClassId,
                    register_number = actualRegisterNumber,
                    name = name,
                    surname = surname,
                    email = actualEmail,
                    phone = actualPhone,
                )
            } else {
                queries.updateStudent(
                    name = name,
                    surname = surname,
                    email = actualEmail,
                    phone = actualPhone,
                    register_number = actualRegisterNumber,
                    id = id,
                )
            }
        }
    }

    override suspend fun deleteStudentById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteStudentById(id)
    }

    // Make sure this method is only used inside transaction.
    @Suppress("UnusedReceiverParameter")
    private fun TransactionWithoutReturn.swapRegisterNumbersIfUsedAndGetAvailableNumber(
        id: Long?,
        schoolClassId: Long,
        registerNumber: Long?,
    ): Long {
        val studentMaxRegisterNumberQuery =
            queries.getStudentMaxRegisterNumberBySchoolClassId(schoolClassId)
        val availableRegisterNumber =
            (studentMaxRegisterNumberQuery.executeAsOne().max_register_number ?: 0L) + 1L
        val usedRegisterNumbers =
            queries.getUsedRegisterNumbersBySchoolClassId(schoolClassId).executeAsList()

        var actualRegisterNumber = registerNumber ?: availableRegisterNumber

        // Register number collision.
        if (registerNumber != null && registerNumber in usedRegisterNumbers) {
            val otherStudent = queries.getStudentByRegisterNumber(registerNumber).executeAsOne()

            if (id == null) {
                // Current student doesn't exist, so we can't simply swap register numbers.
                // Instead set other student register number to new available register number.
                queries.updateStudentRegisterNumberById(
                    register_number = availableRegisterNumber,
                    id = otherStudent.id
                )
            } else if (id != otherStudent.id) {
                // Update other student register number to current student register number.
                val currentRegisterNumber =
                    queries.getBasicStudentById(id).executeAsOne().register_number
                queries.updateStudentRegisterNumberById(
                    register_number = currentRegisterNumber,
                    id = otherStudent.id,
                )
            }

            // Current student should set `registerNumber` (previously used register number).
            actualRegisterNumber = registerNumber
        }

        return actualRegisterNumber
    }
}