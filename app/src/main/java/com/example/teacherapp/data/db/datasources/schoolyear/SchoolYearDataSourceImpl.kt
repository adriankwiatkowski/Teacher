package com.example.teacherapp.data.db.datasources.schoolyear

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.utils.insertAndGetId
import com.example.teacherapp.data.db.datasources.utils.querymappers.SchoolYearMapper
import com.example.teacherapp.data.di.DispatcherProvider
import com.example.teacherapp.data.models.entities.SchoolYear
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate

class SchoolYearDataSourceImpl(
    db: TeacherDatabase,
    private val dispatchers: DispatcherProvider,
) : SchoolYearDataSource {

    private val queries = db.schoolYearQueries
    private val common = db.commonQueries

    override suspend fun getSchoolYearById(id: Long): Flow<SchoolYear?> {
        return withContext(dispatchers.io) {
            queries.getSchoolYearById(id, SchoolYearMapper::mapSchoolYear)
                .asFlow()
                .mapToOneOrNull()
        }
    }

    override fun getAllSchoolYears(): Flow<List<SchoolYear>> {
        return queries.getAllSchoolYears(SchoolYearMapper::mapSchoolYear).asFlow().mapToList()
    }

    override suspend fun insertSchoolYear(
        schoolYearName: String,
        termFirstName: String,
        termFirstStartDate: LocalDate,
        termFirstEndDate: LocalDate,
        termSecondName: String,
        termSecondStartDate: LocalDate,
        termSecondEndDate: LocalDate,
    ) {
        withContext(dispatchers.io) {
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
    }

    override suspend fun deleteSchoolYearById(id: Long) {
        withContext(dispatchers.io) {
            queries.deleteSchoolYearById(id)
        }
    }
}