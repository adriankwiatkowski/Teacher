package com.example.teacherapp.core.data.di

import com.example.teacherapp.core.data.repository.gradetemplate.DatabaseGradeTemplateRepository
import com.example.teacherapp.core.data.repository.gradetemplate.GradeTemplateRepository
import com.example.teacherapp.core.data.repository.lesson.DatabaseLessonRepository
import com.example.teacherapp.core.data.repository.lesson.LessonRepository
import com.example.teacherapp.core.data.repository.schoolclass.DatabaseSchoolClassRepository
import com.example.teacherapp.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacherapp.core.data.repository.schoolyear.DatabaseSchoolYearRepository
import com.example.teacherapp.core.data.repository.schoolyear.SchoolYearRepository
import com.example.teacherapp.core.data.repository.student.DatabaseStudentRepository
import com.example.teacherapp.core.data.repository.student.StudentRepository
import com.example.teacherapp.core.data.repository.studentnote.DatabaseStudentNoteRepository
import com.example.teacherapp.core.data.repository.studentnote.StudentNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsLessonRepository(
        lessonRepository: DatabaseLessonRepository,
    ): LessonRepository

    @Binds
    fun bindsSchoolClassRepository(
        schoolClassRepository: DatabaseSchoolClassRepository,
    ): SchoolClassRepository

    @Binds
    fun bindsSchoolYearRepository(
        schoolYearRepository: DatabaseSchoolYearRepository,
    ): SchoolYearRepository

    @Binds
    fun bindsStudentNoteRepository(
        studentNoteRepository: DatabaseStudentNoteRepository,
    ): StudentNoteRepository

    @Binds
    fun bindsStudentRepository(
        studentRepository: DatabaseStudentRepository,
    ): StudentRepository

    @Binds
    fun bindsGradeTemplateRepository(
        gradeTemplateRepository: DatabaseGradeTemplateRepository,
    ): GradeTemplateRepository
}