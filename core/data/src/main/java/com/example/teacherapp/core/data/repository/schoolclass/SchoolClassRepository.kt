package com.example.teacherapp.core.data.repository.schoolclass

import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.core.model.data.SchoolYear
import kotlinx.coroutines.flow.Flow

interface SchoolClassRepository {

    fun getAllSchoolClasses(): Flow<Result<List<BasicSchoolClass>>>

    fun getSchoolClassById(id: Long): Flow<Result<SchoolClass>>

    fun getAllSchoolYears(): Flow<List<SchoolYear>>

    suspend fun insertSchoolClass(schoolYearId: Long, name: String)

    suspend fun deleteSchoolClassById(id: Long)
}