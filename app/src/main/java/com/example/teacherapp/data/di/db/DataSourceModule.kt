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
import com.example.teacherapp.data.di.DefaultDispatcher
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
}