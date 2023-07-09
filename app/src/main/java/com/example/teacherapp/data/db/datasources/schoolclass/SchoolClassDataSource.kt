package com.example.teacherapp.data.db.datasources.schoolclass

import com.example.teacherapp.data.models.entities.BasicSchoolClass
import com.example.teacherapp.data.models.entities.SchoolClass
import kotlinx.coroutines.flow.Flow

interface SchoolClassDataSource {

    fun getSchoolClassById(id: Long): Flow<SchoolClass?>

    fun getAllSchoolClasses(): Flow<List<BasicSchoolClass>>

    suspend fun insertSchoolClass(schoolYearId: Long, name: String)

    suspend fun deleteSchoolClassById(id: Long)
}