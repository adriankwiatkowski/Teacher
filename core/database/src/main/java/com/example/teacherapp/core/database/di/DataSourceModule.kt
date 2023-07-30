package com.example.teacherapp.core.database.di

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.grade.GradeDataSource
import com.example.teacherapp.core.database.datasource.grade.GradeDataSourceImpl
import com.example.teacherapp.core.database.datasource.gradetemplate.GradeTemplateDataSource
import com.example.teacherapp.core.database.datasource.gradetemplate.GradeTemplateDataSourceImpl
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSource
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSourceImpl
import com.example.teacherapp.core.database.datasource.lessonactivity.LessonActivityDataSource
import com.example.teacherapp.core.database.datasource.lessonactivity.LessonActivityDataSourceImpl
import com.example.teacherapp.core.database.datasource.lessonnote.LessonNoteDataSource
import com.example.teacherapp.core.database.datasource.lessonnote.LessonNoteDataSourceImpl
import com.example.teacherapp.core.database.datasource.note.NoteDataSource
import com.example.teacherapp.core.database.datasource.note.NoteDataSourceImpl
import com.example.teacherapp.core.database.datasource.schoolclass.SchoolClassDataSource
import com.example.teacherapp.core.database.datasource.schoolclass.SchoolClassDataSourceImpl
import com.example.teacherapp.core.database.datasource.schoolyear.SchoolYearDataSource
import com.example.teacherapp.core.database.datasource.schoolyear.SchoolYearDataSourceImpl
import com.example.teacherapp.core.database.datasource.student.StudentDataSource
import com.example.teacherapp.core.database.datasource.student.StudentDataSourceImpl
import com.example.teacherapp.core.database.datasource.studentnote.StudentNoteDataSource
import com.example.teacherapp.core.database.datasource.studentnote.StudentNoteDataSourceImpl
import com.example.teacherapp.core.database.generated.TeacherDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideSchoolYearDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): SchoolYearDataSource {
        return SchoolYearDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideSchoolClassDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): SchoolClassDataSource {
        return SchoolClassDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideStudentDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): StudentDataSource {
        return StudentDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideStudentNoteDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): StudentNoteDataSource {
        return StudentNoteDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideLessonDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): LessonDataSource {
        return LessonDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideGradeTemplateDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): GradeTemplateDataSource {
        return GradeTemplateDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideGradeDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): GradeDataSource {
        return GradeDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideLessonActivityDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): LessonActivityDataSource {
        return LessonActivityDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideLessonNoteDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): LessonNoteDataSource {
        return LessonNoteDataSourceImpl(db, dispatcher)
    }

    @Provides
    @Singleton
    fun provideNoteDataSource(
        db: TeacherDatabase,
        @DefaultDispatcher dispatcher: CoroutineDispatcher,
    ): NoteDataSource {
        return NoteDataSourceImpl(db, dispatcher)
    }
}