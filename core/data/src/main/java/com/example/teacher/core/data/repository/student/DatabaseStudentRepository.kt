package com.example.teacher.core.data.repository.student

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.common.result.asResultNotNull
import com.example.teacher.core.database.datasource.student.StudentDataSource
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGradesByLesson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class DatabaseStudentRepository @Inject constructor(
    private val dataSource: StudentDataSource,
    @ApplicationScope private val scope: CoroutineScope,
) : StudentRepository {

    override fun getStudentOrNullById(id: Long): Flow<Result<Student?>> = dataSource
        .getStudentById(id)
        .asResult()

    override fun getStudentById(id: Long): Flow<Result<Student>> = dataSource
        .getStudentById(id)
        .asResultNotNull()

    override fun getUsedRegisterNumbersBySchoolClassId(
        schoolClassId: Long
    ): Flow<Result<List<Long>>> = dataSource
        .getUsedRegisterNumbersBySchoolClassId(schoolClassId)
        .asResult()

    override fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?> = dataSource
        .getStudentSchoolClassNameById(schoolClassId)

    override fun getStudentGradesById(
        studentId: Long
    ): Flow<Result<List<StudentGradesByLesson>>> = dataSource
        .getStudentGradesById(studentId)
        .asResult()

    override suspend fun upsertStudent(
        id: Long?,
        schoolClassId: Long,
        registerNumber: Long?,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    ) {
        scope.launch {
            dataSource.upsertStudent(
                id = id,
                schoolClassId = schoolClassId,
                registerNumber = registerNumber,
                name = name,
                surname = surname,
                email = email,
                phone = phone,
            )
        }
    }

    override suspend fun deleteStudentById(id: Long) {
        scope.launch {
            dataSource.deleteStudentById(id)
        }
    }
}