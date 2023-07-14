package com.example.teacherapp.data.db.datasources.schoolyear

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.utils.insertAndGetId
import com.example.teacherapp.data.db.datasources.utils.querymappers.SchoolYearMapper
import com.example.teacherapp.data.di.DefaultDispatcher
import com.example.teacherapp.data.models.entities.SchoolYear
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate

class SchoolYearDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : SchoolYearDataSource {

    private val queries = db.schoolYearQueries
    private val common = db.commonQueries

    override suspend fun getSchoolYearById(id: Long): Flow<SchoolYear?> =
        queries
            .getSchoolYearById(id, SchoolYearMapper::mapSchoolYear)
            .asFlow()
            .mapToOneOrNull()

    override fun getAllSchoolYears(): Flow<List<SchoolYear>> =
        queries
            .getAllSchoolYears(SchoolYearMapper::mapSchoolYear)
            .asFlow()
            .mapToList()

    override suspend fun insertSchoolYear(
        schoolYearName: String,
        termFirstName: String,
        termFirstStartDate: LocalDate,
        termFirstEndDate: LocalDate,
        termSecondName: String,
        termSecondStartDate: LocalDate,
        termSecondEndDate: LocalDate,
    ): Unit = withContext(dispatcher) {
        queries.transaction {
            val termFirstId = common.insertAndGetId {
                queries.insertTerm(
                    id = null,
                    name = termFirstName,
                    start_date = termFirstStartDate,
                    end_date = termFirstEndDate,
                )
            }
            val termSecondId = common.insertAndGetId {
                queries.insertTerm(
                    id = null,
                    name = termSecondName,
                    start_date = termSecondStartDate,
                    end_date = termSecondEndDate,
                )
            }

            common.insertAndGetId {
                queries.insertSchoolYear(
                    id = null,
                    term_first_id = termFirstId,
                    term_second_id = termSecondId,
                    name = schoolYearName,
                )
            }
        }
    }

    override suspend fun deleteSchoolYearById(id: Long): Unit = withContext(dispatcher) {
        queries.deleteSchoolYearById(id)
    }
}