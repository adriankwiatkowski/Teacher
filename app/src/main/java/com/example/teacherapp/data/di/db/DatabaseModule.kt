package com.example.teacherapp.data.di.db

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.teacherapp.data.db.TeacherDatabase
import com.example.teacherapp.data.db.adapters.BigDecimalColumnAdapter
import com.example.teacherapp.data.db.adapters.DateColumnAdapter
import com.example.teacherapp.data.db.adapters.TimeColumnAdapter
import com.example.teacherapp.data.db.models.Grade
import com.example.teacherapp.data.db.models.Grade_lesson
import com.example.teacherapp.data.db.models.Lesson_calendar
import com.example.teacherapp.data.db.models.Term
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = TeacherDatabase.Schema,
            context = app,
            name = "teacher.db",
            callback = object : AndroidSqliteDriver.Callback(TeacherDatabase.Schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }

    @Provides
    @Singleton
    fun provideTeacherDatabase(driver: SqlDriver): TeacherDatabase {
        val bigDecimalAdapter = BigDecimalColumnAdapter
        val dateAdapter = DateColumnAdapter
        val timeAdapter = TimeColumnAdapter

        return TeacherDatabase(
            driver = driver,
            gradeAdapter = Grade.Adapter(
                gradeAdapter = bigDecimalAdapter,
                dateAdapter = dateAdapter,
            ),
            grade_lessonAdapter = Grade_lesson.Adapter(
                dateAdapter = dateAdapter,
            ),
            lesson_calendarAdapter = Lesson_calendar.Adapter(
                dateAdapter = dateAdapter,
                start_timeAdapter = timeAdapter,
                end_timeAdapter = timeAdapter,
            ),
            termAdapter = Term.Adapter(
                start_dateAdapter = dateAdapter,
                end_dateAdapter = dateAdapter,
            ),
        )
    }
}