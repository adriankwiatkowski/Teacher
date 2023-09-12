package com.example.teacher.core.data.repository.schoolclass

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.common.result.asResultNotNull
import com.example.teacher.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacher.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.SchoolClass
import com.example.teacher.core.model.data.SchoolClassesByYear
import com.example.teacher.core.model.data.SchoolYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseSchoolClassRepository @Inject constructor(
    private val dataSource: SchoolClassDataSource,
    private val schoolYearDataSource: SchoolYearDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : SchoolClassRepository {

    override fun getAllSchoolClasses(): Flow<Result<List<SchoolClassesByYear>>> = dataSource
        .getAllSchoolClasses()
        .asResult()

    override fun getBasicSchoolClassOrNullById(
        id: Long
    ): Flow<Result<BasicSchoolClass?>> = dataSource
        .getBasicSchoolClassById(id)
        .asResult()

    override fun getSchoolClassById(id: Long): Flow<Result<SchoolClass>> = dataSource
        .getSchoolClassById(id)
        .asResultNotNull()

    override fun getAllSchoolYears(): Flow<List<SchoolYear>> =
        schoolYearDataSource.getAllSchoolYears()

    override suspend fun insertOrUpdateSchoolClass(id: Long?, schoolYearId: Long, name: String) {
        scope.launch {
            dataSource.insertOrUpdateSchoolClass(id = id, schoolYearId = schoolYearId, name = name)
        }
    }

    override suspend fun deleteSchoolClassById(id: Long) {
        scope.launch {
            dataSource.deleteSchoolClassById(id)
        }
    }
}