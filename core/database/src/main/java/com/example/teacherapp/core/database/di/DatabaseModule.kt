package com.example.teacherapp.core.database.di

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.teacherapp.core.database.adapter.BigDecimalColumnAdapter
import com.example.teacherapp.core.database.adapter.DateColumnAdapter
import com.example.teacherapp.core.database.adapter.TimeColumnAdapter
import com.example.teacherapp.core.database.generated.TeacherDatabase
import com.example.teacherapp.core.database.generated.model.Grade
import com.example.teacherapp.core.database.generated.model.Grade_template
import com.example.teacherapp.core.database.generated.model.Lesson_schedule
import com.example.teacherapp.core.database.generated.model.Term
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
            grade_templateAdapter = Grade_template.Adapter(
                dateAdapter = dateAdapter,
            ),
            lesson_scheduleAdapter = Lesson_schedule.Adapter(
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