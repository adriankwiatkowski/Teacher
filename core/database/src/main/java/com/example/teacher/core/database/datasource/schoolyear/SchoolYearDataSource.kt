package com.example.teacher.core.database.datasource.schoolyear

import com.example.teacher.core.model.data.SchoolYear
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface SchoolYearDataSource {

    fun getAllSchoolYears(): Flow<List<SchoolYear>>

    fun getSchoolYearById(id: Long): Flow<SchoolYear?>

    suspend fun getSchoolYearByLessonId(lessonId: Long): SchoolYear?

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