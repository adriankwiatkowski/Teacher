package com.example.teacher.core.data.repository.schoolyear

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.SchoolYear
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SchoolYearRepository {

    suspend fun getSchoolYearById(id: Long): Flow<Result<SchoolYear?>>

    fun getAllSchoolYears(): Flow<List<SchoolYear>>

    suspend fun insertOrUpdateSchoolYear(
        id: Long?,
        schoolYearName: String,
        termFirstName: String,
        termFirstStartDate: LocalDate,
        termFirstEndDate: LocalDate,
        termSecondName: String,
        termSecondStartDate: LocalDate,
        termSecondEndDate: LocalDate,
    )

    suspend fun deleteSchoolYearById(id: Long)
}