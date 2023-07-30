package com.example.teacherapp.core.database.datasource.schoolclass

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.SchoolClass
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class SchoolClassDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : SchoolClassDataSource {

    private val schoolClassQueries = db.schoolClassQueries
    private val studentQueries = db.studentQueries
    private val lessonQueries = db.lessonQueries

    override fun getSchoolClassById(id: Long): Flow<SchoolClass?> {
        val schoolClassFlow = schoolClassQueries
            .getSchoolClassById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
        val studentsFlow = studentQueries
            .getStudentsBySchoolClassId(id)
            .asFlow()
            .mapToList(dispatcher)
        val lessonsFlow = lessonQueries
            .getLessonsBySchoolClassId(id)
            .asFlow()
            .mapToList(dispatcher)

        return combine(
            schoolClassFlow,
            studentsFlow,
            lessonsFlow,
            ::toExternal,
        ).flowOn(dispatcher)
    }

    override fun getAllSchoolClasses(): Flow<List<BasicSchoolClass>> =
        schoolClassQueries
            .getAllSchoolClasses()
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override suspend fun insertSchoolClass(
        schoolYearId: Long,
        name: String,
    ): Unit = withContext(dispatcher) {
        schoolClassQueries.insertSchoolClass(
            id = null,
            school_year_id = schoolYearId,
            name = name,
        )
    }

    override suspend fun deleteSchoolClassById(id: Long): Unit = withContext(dispatcher) {
        schoolClassQueries.deleteSchoolClassById(id)
    }
}