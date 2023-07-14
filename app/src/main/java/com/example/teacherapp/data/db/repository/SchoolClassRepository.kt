package com.example.teacherapp.data.db.repository

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.core.model.data.SchoolYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchoolClassRepository @Inject constructor(
    private val dataSource: SchoolClassDataSource,
    private val schoolYearRepository: SchoolYearRepository,
    @ApplicationScope private val scope: CoroutineScope,
) {

    fun getAllSchoolClasses(): Flow<Result<List<BasicSchoolClass>>> = dataSource
        .getAllSchoolClasses()
        .asResult()

    fun getSchoolClassById(id: Long): Flow<Result<SchoolClass>> = dataSource
        .getSchoolClassById(id)
        .asResultNotNull()

    fun getAllSchoolYears(): Flow<List<SchoolYear>> = schoolYearRepository.getAllSchoolYears()

    suspend fun insertSchoolClass(schoolYearId: Long, name: String) {
        scope.launch {
            dataSource.insertSchoolClass(schoolYearId = schoolYearId, name = name)
        }
    }

    suspend fun deleteSchoolClassById(id: Long) {
        scope.launch {
            dataSource.deleteSchoolClassById(id)
        }
    }
}