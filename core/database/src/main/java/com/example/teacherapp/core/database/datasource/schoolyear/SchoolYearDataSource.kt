package com.example.teacherapp.core.database.datasource.schoolyear

import com.example.teacherapp.core.model.data.SchoolYear
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SchoolYearDataSource {

    suspend fun getSchoolYearById(id: Long): Flow<SchoolYear?>

    fun getAllSchoolYears(): Flow<List<SchoolYear>>

    suspend fun insertSchoolYear(
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