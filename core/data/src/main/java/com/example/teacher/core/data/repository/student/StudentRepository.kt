package com.example.teacher.core.data.repository.student

import com.example.teacher.core.common.result.Result
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGradesByLesson
import kotlinx.coroutines.flow.Flow

interface StudentRepository {

    fun getStudentOrNullById(id: Long): Flow<Result<Student?>>

    fun getStudentById(id: Long): Flow<Result<Student>>

    fun getUsedRegisterNumbersBySchoolClassId(schoolClassId: Long): Flow<Result<List<Long>>>

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?>

    fun getStudentGradesById(studentId: Long): Flow<Result<List<StudentGradesByLesson>>>

    suspend fun insertOrUpdateStudent(
        id: Long?,
        schoolClassId: Long,
        registerNumber: Long?,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    )

    suspend fun deleteStudentById(id: Long)
}