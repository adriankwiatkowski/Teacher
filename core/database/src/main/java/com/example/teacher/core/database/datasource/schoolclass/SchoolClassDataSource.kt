package com.example.teacher.core.database.datasource.schoolclass

import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolClass
import kotlinx.coroutines.flow.Flow

interface SchoolClassDataSource {

    fun getSchoolClassById(id: Long): Flow<SchoolClass?>

    fun getAllSchoolClasses(): Flow<List<BasicSchoolClass>>

    suspend fun insertSchoolClass(schoolYearId: Long, name: String)

    suspend fun deleteSchoolClassById(id: Long)
}