package com.example.teacher.core.database.di

import app.cash.sqldelight.db.SqlDriver
import com.example.teacher.core.database.adapter.BigDecimalColumnAdapter
import com.example.teacher.core.database.adapter.DateColumnAdapter
import com.example.teacher.core.database.adapter.TimeColumnAdapter
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.generated.model.Event
import com.example.teacher.core.database.generated.model.Grade
import com.example.teacher.core.database.generated.model.Grade_template
import com.example.teacher.core.database.generated.model.Term
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
            eventAdapter = Event.Adapter(
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

    fun createSchema(driver: SqlDriver) {
        TeacherDatabase.Schema.create(driver)
    }
}