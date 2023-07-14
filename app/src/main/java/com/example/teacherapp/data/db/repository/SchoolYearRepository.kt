package com.example.teacherapp.data.db.repository

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacherapp.core.model.data.SchoolYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

class SchoolYearRepository @Inject constructor(
    private val dataSource: SchoolYearDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) {

    suspend fun getSchoolYearById(id: Long): Flow<Result<SchoolYear?>> = dataSource
        .getSchoolYearById(id)
        .asResult()

    fun getAllSchoolYears(): Flow<List<SchoolYear>> = dataSource.getAllSchoolYears()

    suspend fun insertSchoolYear(
        schoolYearName: String,
        termFirstName: String,
        termFirstStartDate: LocalDate,
        termFirstEndDate: LocalDate,
        termSecondName: String,
        termSecondStartDate: LocalDate,
        termSecondEndDate: LocalDate,
    ) {
        scope.launch {
            dataSource.insertSchoolYear(
                schoolYearName = schoolYearName,
                termFirstName = termFirstName,
                termFirstStartDate = termFirstStartDate,
                termFirstEndDate = termFirstEndDate,
                termSecondName = termSecondName,
                termSecondStartDate = termSecondStartDate,
                termSecondEndDate = termSecondEndDate,
            )
        }
    }

    suspend fun deleteSchoolYearById(id: Long) {
        scope.launch {
            dataSource.deleteSchoolYearById(id)
        }
    }
}