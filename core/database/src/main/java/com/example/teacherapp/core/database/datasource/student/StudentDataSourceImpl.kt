package com.example.teacherapp.core.database.datasource.student

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternalStudentGrades
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Student
import com.example.teacherapp.core.model.data.StudentGradesByLesson
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
        orderInClass: Long?,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    ): Unit = withContext(dispatcher) {
        val defaultOrderInClass = 1L

        if (id == null) {
            queries.insertStudent(
                id = null,
                school_class_id = schoolClassId,
                order_in_class = orderInClass ?: defaultOrderInClass,
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
                order_in_class = orderInClass ?: defaultOrderInClass,
                id = id,
            )
        }
    }

    override suspend fun deleteStudentById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteStudentById(id)
    }
}