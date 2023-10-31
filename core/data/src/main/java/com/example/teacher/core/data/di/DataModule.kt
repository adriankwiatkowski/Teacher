package com.example.teacher.core.data.di

import com.example.teacher.core.data.repository.event.DatabaseEventRepository
import com.example.teacher.core.data.repository.event.EventRepository
import com.example.teacher.core.data.repository.grade.DatabaseGradeRepository
import com.example.teacher.core.data.repository.grade.GradeRepository
import com.example.teacher.core.data.repository.gradescore.DatabaseGradeScoreRepository
import com.example.teacher.core.data.repository.gradescore.GradeScoreRepository
import com.example.teacher.core.data.repository.gradetemplate.DatabaseGradeTemplateRepository
import com.example.teacher.core.data.repository.gradetemplate.GradeTemplateRepository
import com.example.teacher.core.data.repository.lesson.DatabaseLessonRepository
import com.example.teacher.core.data.repository.lesson.LessonRepository
import com.example.teacher.core.data.repository.lessonactivity.DatabaseLessonActivityRepository
import com.example.teacher.core.data.repository.lessonactivity.LessonActivityRepository
import com.example.teacher.core.data.repository.lessonattendance.DatabaseLessonAttendanceRepository
import com.example.teacher.core.data.repository.lessonattendance.LessonAttendanceRepository
import com.example.teacher.core.data.repository.lessonnote.DatabaseLessonNoteRepository
import com.example.teacher.core.data.repository.lessonnote.LessonNoteRepository
import com.example.teacher.core.data.repository.note.DatabaseNoteRepository
import com.example.teacher.core.data.repository.note.NoteRepository
import com.example.teacher.core.data.repository.schoolclass.DatabaseSchoolClassRepository
import com.example.teacher.core.data.repository.schoolclass.SchoolClassRepository
import com.example.teacher.core.data.repository.schoolyear.DatabaseSchoolYearRepository
import com.example.teacher.core.data.repository.schoolyear.SchoolYearRepository
import com.example.teacher.core.data.repository.settings.LocalSettingsRepository
import com.example.teacher.core.data.repository.settings.SettingsRepository
import com.example.teacher.core.data.repository.student.DatabaseStudentRepository
import com.example.teacher.core.data.repository.student.StudentRepository
import com.example.teacher.core.data.repository.studentnote.DatabaseStudentNoteRepository
import com.example.teacher.core.data.repository.studentnote.StudentNoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsEventRepository(
        eventRepository: DatabaseEventRepository,
    ): EventRepository

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

    @Binds
    fun bindsGradeScoreRepository(
        gradeScoreRepository: DatabaseGradeScoreRepository,
    ): GradeScoreRepository

    @Binds
    fun bindsGradeRepository(
        gradeTemplateRepository: DatabaseGradeRepository,
    ): GradeRepository

    @Binds
    fun bindsLessonActivityRepository(
        lessonActivityRepository: DatabaseLessonActivityRepository,
    ): LessonActivityRepository

    @Binds
    fun bindsLessonAttendanceRepository(
        lessonAttendanceRepository: DatabaseLessonAttendanceRepository,
    ): LessonAttendanceRepository

    @Binds
    fun bindsSettingsRepository(
        settingsRepository: LocalSettingsRepository,
    ): SettingsRepository

    @Binds
    fun bindsNoteRepository(
        noteRepository: DatabaseNoteRepository,
    ): NoteRepository

    @Binds
    fun bindsLessonNoteRepository(
        lessonNoteRepository: DatabaseLessonNoteRepository,
    ): LessonNoteRepository
}