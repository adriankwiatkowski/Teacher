package com.example.teacherapp.core.database.datasource.student

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymappers.SchoolClassMapper
import com.example.teacherapp.core.database.datasource.utils.querymappers.StudentMapper
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicStudent
import com.example.teacherapp.core.model.data.Student
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class StudentDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : StudentDataSource {

    private val queries = db.studentQueries
    private val schoolClassQueries = db.schoolClassQueries

    override fun getStudentById(id: Long): Flow<Student?> =
        queries
            .getStudentById(id) { _, orderInClass, schoolClassId, name, surname, email, phone, schoolClassName ->
                val schoolClass = SchoolClassMapper.mapBasicSchoolClass(
                    id = schoolClassId,
                    name = schoolClassName,
                )
                Student(
                    id = id,
                    name = name,
                    orderInClass = orderInClass,
                    surname = surname,
                    email = email,
                    phone = phone,
                    schoolClass = schoolClass,
                    grades = emptyList(),
                )
            }
            .asFlow()
            .mapToOneOrNull()

    override fun getStudentsBySchoolClassId(schoolClassId: Long): Flow<List<BasicStudent>> =
        queries
            .getStudentsBySchoolClassId(schoolClassId, StudentMapper::mapBasicStudent)
            .asFlow()
            .mapToList()

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> =
        schoolClassQueries.getSchoolClassNameById(schoolClassId)
            .asFlow()
            .mapToOneOrNull()

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