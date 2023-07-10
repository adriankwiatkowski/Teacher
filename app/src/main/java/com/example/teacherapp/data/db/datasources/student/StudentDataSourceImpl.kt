package com.example.teacherapp.data.db.datasources.student

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.utils.querymappers.SchoolClassMapper
import com.example.teacherapp.data.db.datasources.utils.querymappers.StudentClassMapper
import com.example.teacherapp.data.di.DispatcherProvider
import com.example.teacherapp.data.models.entities.BasicStudent
import com.example.teacherapp.data.models.entities.Student
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class StudentDataSourceImpl(
    db: TeacherDatabase,
    private val dispatchers: DispatcherProvider,
) : StudentDataSource {

    private val queries = db.studentQueries
    private val schoolClassQueries = db.schoolClassQueries

    override fun getStudentById(id: Long): Flow<Student?> {
        return queries
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
    }

    override fun getStudentsBySchoolClassId(schoolClassId: Long): Flow<List<BasicStudent>> {
        return queries
            .getStudentsBySchoolClassId(schoolClassId, StudentClassMapper::mapBasicStudent)
            .asFlow()
            .mapToList()
    }

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> {
        return schoolClassQueries.getSchoolClassNameById(schoolClassId)
            .asFlow()
            .mapToOneOrNull()
    }

    override suspend fun insertOrUpdateStudent(
        id: Long?,
        schoolClassId: Long,
        orderInClass: Long?,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    ): Unit = withContext(dispatchers.io) {
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

    override suspend fun deleteStudentById(id: Long): Unit = withContext(dispatchers.io) {
        queries.deleteStudentById(id)
    }
}