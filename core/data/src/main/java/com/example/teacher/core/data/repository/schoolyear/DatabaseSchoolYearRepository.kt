package com.example.teacher.core.data.repository.schoolyear

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.model.data.SchoolYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

internal class DatabaseSchoolYearRepository @Inject constructor(
    private val dataSource: SchoolYearDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : SchoolYearRepository {

    override suspend fun getSchoolYearById(id: Long): Flow<Result<SchoolYear?>> = dataSource
        .getSchoolYearById(id)
        .asResult()

    override fun getAllSchoolYears(): Flow<List<SchoolYear>> = dataSource.getAllSchoolYears()

    override suspend fun insertOrUpdateSchoolYear(
        id: Long?,
        schoolYearName: String,
        termFirstId: Long?,
        termFirstName: String,
        termFirstStartDate: LocalDate,
        termFirstEndDate: LocalDate,
        termSecondId: Long?,
        termSecondName: String,
        termSecondStartDate: LocalDate,
        termSecondEndDate: LocalDate,
    ) {
        scope.launch {
            dataSource.insertOrUpdateSchoolYear(
                id = id,
                schoolYearName = schoolYearName,
                termFirstId = termFirstId,
                termFirstName = termFirstName,
                termFirstStartDate = termFirstStartDate,
                termFirstEndDate = termFirstEndDate,
                termSecondId = termSecondId,
                termSecondName = termSecondName,
                termSecondStartDate = termSecondStartDate,
                termSecondEndDate = termSecondEndDate,
            )
        }
    }

    override suspend fun deleteSchoolYearById(id: Long) {
        scope.launch {
            dataSource.deleteSchoolYearById(id)
        }
    }
}