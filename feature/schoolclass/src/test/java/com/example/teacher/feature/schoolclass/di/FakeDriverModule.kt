package com.example.teacher.feature.schoolclass.di

import app.cash.sqldelight.db.SqlDriver
import com.example.teacher.core.database.di.DatabaseModule
import com.example.teacher.core.database.di.DriverModule
import com.example.teacher.core.testing.util.TestDatabaseDriverProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DriverModule::class]
)
object FakeDriverModule {

    @Provides
    @Singleton
    fun provideSqlDriver(): SqlDriver {
        return TestDatabaseDriverProvider.createDatabaseDriver()
            .also { DatabaseModule.createSchema(it) }
    }
}