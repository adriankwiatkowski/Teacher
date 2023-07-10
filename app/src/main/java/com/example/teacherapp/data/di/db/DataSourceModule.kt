package com.example.teacherapp.data.di.db

import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.datasources.lesson.LessonDataSource
import com.example.teacherapp.data.db.datasources.lesson.LessonDataSourceImpl
import com.example.teacherapp.data.db.datasources.schoolclass.SchoolClassDataSource
import com.example.teacherapp.data.db.datasources.schoolclass.SchoolClassDataSourceImpl
import com.example.teacherapp.data.db.datasources.schoolyear.SchoolYearDataSource
import com.example.teacherapp.data.db.datasources.schoolyear.SchoolYearDataSourceImpl
import com.example.teacherapp.data.db.datasources.student.StudentDataSource
import com.example.teacherapp.data.db.datasources.student.StudentDataSourceImpl
import com.example.teacherapp.data.db.datasources.student.note.StudentNoteDataSource
import com.example.teacherapp.data.db.datasources.student.note.StudentNoteDataSourceImpl
import com.example.teacherapp.data.di.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideSchoolYearDataSource(
        db: TeacherDatabase,
        dispatchers: DispatcherProvider,
    ): SchoolYearDataSource {
        return SchoolYearDataSourceImpl(db, dispatchers)
    }

    @Provides
    @Singleton
    fun provideSchoolClassDataSource(
        db: TeacherDatabase,
        dispatchers: DispatcherProvider,
    ): SchoolClassDataSource {
        return SchoolClassDataSourceImpl(db, dispatchers)
    }

    @Provides
    @Singleton
    fun provideStudentDataSource(
        db: TeacherDatabase,
        dispatchers: DispatcherProvider,
    ): StudentDataSource {
        return StudentDataSourceImpl(db, dispatchers)
    }

    @Provides
    @Singleton
    fun provideStudentNoteDataSource(
        db: TeacherDatabase,
        dispatchers: DispatcherProvider,
    ): StudentNoteDataSource {
        return StudentNoteDataSourceImpl(db, dispatchers)
    }

    @Provides
    @Singleton
    fun provideLessonDataSource(
        db: TeacherDatabase,
        dispatchers: DispatcherProvider,
    ): LessonDataSource {
        return LessonDataSourceImpl(db, dispatchers)
    }
}