package com.example.teacher.core.database.datasource.student

import com.example.teacher.core.model.data.BasicStudent
import com.example.teacher.core.model.data.Student
import com.example.teacher.core.model.data.StudentGradesByLesson
import kotlinx.coroutines.flow.Flow

interface StudentDataSource {

    fun getBasicStudentById(id: Long): Flow<BasicStudent?>

    fun getStudentById(id: Long): Flow<Student?>

    fun getStudentsBySchoolClassId(schoolClassId: Long): Flow<List<BasicStudent>>

    fun getUsedRegisterNumbersBySchoolClassId(schoolClassId: Long): Flow<List<Long>>

    fun getStudentSchoolClassNameById(schoolClassId: Long): Flow<String?>

    fun getStudentGradesById(studentId: Long): Flow<List<StudentGradesByLesson>>

    suspend fun upsertStudent(
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