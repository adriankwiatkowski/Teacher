package com.example.teacherapp.core.data.repository.schoolclass

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.common.result.asResultNotNull
import com.example.teacherapp.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacherapp.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacherapp.core.model.data.BasicSchoolClass
import com.example.teacherapp.core.model.data.SchoolClass
import com.example.teacherapp.core.model.data.SchoolYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseSchoolClassRepository @Inject constructor(
    private val dataSource: SchoolClassDataSource,
    private val schoolYearDataSource: SchoolYearDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : SchoolClassRepository {

    override fun getAllSchoolClasses(): Flow<Result<List<BasicSchoolClass>>> = dataSource
        .getAllSchoolClasses()
        .asResult()

    override fun getSchoolClassById(id: Long): Flow<Result<SchoolClass>> = dataSource
        .getSchoolClassById(id)
        .asResultNotNull()

    override fun getAllSchoolYears(): Flow<List<SchoolYear>> =
        schoolYearDataSource.getAllSchoolYears()

    override suspend fun insertSchoolClass(schoolYearId: Long, name: String) {
        scope.launch {
            dataSource.insertSchoolClass(schoolYearId = schoolYearId, name = name)
        }
    }

    override suspend fun deleteSchoolClassById(id: Long) {
        scope.launch {
            dataSource.deleteSchoolClassById(id)
        }
    }
}