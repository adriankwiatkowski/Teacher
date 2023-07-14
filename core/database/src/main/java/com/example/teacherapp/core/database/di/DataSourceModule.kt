package com.example.teacherapp.core.database.di

import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSource
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSourceImpl
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
}