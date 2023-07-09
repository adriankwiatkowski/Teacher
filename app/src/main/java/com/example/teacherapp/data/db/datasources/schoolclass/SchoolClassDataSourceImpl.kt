package com.example.teacherapp.data.db.datasources.schoolclass

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.utils.querymappers.LessonMapper
import com.example.teacherapp.data.db.datasources.utils.querymappers.SchoolClassMapper
import com.example.teacherapp.data.db.datasources.utils.querymappers.StudentClassMapper
import com.example.teacherapp.data.di.DispatcherProvider
import com.example.teacherapp.data.models.entities.BasicSchoolClass
import com.example.teacherapp.data.models.entities.SchoolClass
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class SchoolClassDataSourceImpl(
    db: TeacherDatabase,
    private val dispatchers: DispatcherProvider,
) : SchoolClassDataSource {

    private val schoolClassQueries = db.schoolClassQueries
    private val studentQueries = db.studentQueries
    private val lessonQueries = db.lessonQueries

    override fun getSchoolClassById(id: Long): Flow<SchoolClass?> {
        val schoolClassFlow = schoolClassQueries
            .getSchoolClassById(id, SchoolClassMapper::mapSchoolClass)
            .asFlow()
            .mapToOneOrNull()
        val studentsFlow = studentQueries
            .getStudentsBySchoolClassId(id, StudentClassMapper::mapBasicStudent)
            .asFlow()
            .mapToList()
        val lessonsFlow = lessonQueries
            .getLessonsBySchoolClassId(id, LessonMapper::mapBasicLesson)
            .asFlow()
            .mapToList()

        return combine(
            schoolClassFlow,
            studentsFlow,
            lessonsFlow,
        ) { schoolClass, students, lessons ->
            schoolClass?.copy(
                students = students,
                lessons = lessons,
            )
        }
    }

    override fun getAllSchoolClasses(): Flow<List<BasicSchoolClass>> {
        return schoolClassQueries
            .getAllSchoolClasses(SchoolClassMapper::mapBasicSchoolClass)
            .asFlow()
            .mapToList()
    }

    override suspend fun insertSchoolClass(schoolYearId: Long, name: String) {
        withContext(dispatchers.io) {
            schoolClassQueries.insertSchoolClass(
                id = null,
                school_year_id = schoolYearId,
                name = name,
            )
        }
    }

    override suspend fun deleteSchoolClassById(id: Long) {
        withContext(dispatchers.io) {
            schoolClassQueries.deleteSchoolClassById(id)
        }
    }
}