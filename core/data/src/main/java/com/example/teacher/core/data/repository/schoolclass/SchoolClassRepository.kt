package com.example.teacher.core.data.repository.schoolclass

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.core.model.data.SchoolClassesByYear
import com.example.teacher.core.model.data.SchoolYear
import kotlinx.coroutines.flow.Flow

interface SchoolClassRepository {

    fun getAllSchoolClasses(): Flow<Result<List<SchoolClassesByYear>>>

    fun getBasicSchoolClassOrNullById(id: Long): Flow<Result<BasicSchoolClass?>>

    fun getSchoolClassById(id: Long): Flow<Result<SchoolClass>>

    fun getAllSchoolYears(): Flow<List<SchoolYear>>

    suspend fun upsertSchoolClass(id: Long?, schoolYearId: Long, name: String)

    suspend fun deleteSchoolClassById(id: Long)
}