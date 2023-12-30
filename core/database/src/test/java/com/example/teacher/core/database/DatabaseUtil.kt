package com.example.teacher.core.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import com.example.teacher.core.database.di.DatabaseModule
import com.example.teacher.core.database.generated.TeacherDatabase

internal fun createTeacherDatabase(): TeacherDatabase {
    val driver = LogSqliteDriver(JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)) {
        val containsInsert = it.contains("INSERT", ignoreCase = true)
        val containsSelect = it.contains("SELECT", ignoreCase = true)
        if (containsInsert || containsSelect) {
            println(it)
        }
    }

    TeacherDatabase.Schema.create(driver)
    return DatabaseModule.provideTeacherDatabase(driver)
}