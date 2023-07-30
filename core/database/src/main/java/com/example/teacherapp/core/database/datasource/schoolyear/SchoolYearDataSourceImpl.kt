package com.example.teacherapp.core.database.datasource.schoolyear

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.utils.insertAndGetId
import com.example.teacherapp.core.database.datasource.utils.querymapper.toExternal
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.model.data.SchoolYear
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate

internal class SchoolYearDataSourceImpl(
    db: TeacherDatabase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : SchoolYearDataSource {

    private val queries = db.schoolYearQueries
    private val common = db.commonQueries

    override suspend fun getSchoolYearById(id: Long): Flow<SchoolYear?> =
        queries
            .getSchoolYearById(id)
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

    override fun getAllSchoolYears(): Flow<List<SchoolYear>> =
        queries
            .getAllSchoolYears()
            .asFlow()
            .mapToList(dispatcher)
            .map(::toExternal)
            .flowOn(dispatcher)

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