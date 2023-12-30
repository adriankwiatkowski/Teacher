package com.example.teacher.core.testing.util

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver

object TestDatabaseDriverProvider {

    fun createDatabaseDriver(): SqlDriver {
        return LogSqliteDriver(JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)) {
            val containsInsert = it.contains("INSERT", ignoreCase = true)
            val containsSelect = it.contains("SELECT", ignoreCase = true)
            if (containsInsert || containsSelect) {
                println(it)
            }
        }
    }
}