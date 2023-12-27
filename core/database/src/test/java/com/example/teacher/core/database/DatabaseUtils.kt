package com.example.teacher.core.database

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.sqldelight.logs.LogSqliteDriver
import com.example.teacher.core.database.adapter.BigDecimalColumnAdapter
import com.example.teacher.core.database.adapter.DateColumnAdapter
import com.example.teacher.core.database.adapter.TimeColumnAdapter
import com.example.teacher.core.database.generated.TeacherDatabase
import com.example.teacher.core.database.generated.model.Event
import com.example.teacher.core.database.generated.model.Grade
import com.example.teacher.core.database.generated.model.Grade_template
import com.example.teacher.core.database.generated.model.Term

internal fun createTeacherDatabase(): TeacherDatabase {
    val driver = LogSqliteDriver(JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)) {
        val containsInsert = it.contains("INSERT", ignoreCase = true)
        val containsSelect = it.contains("SELECT", ignoreCase = true)
        if (containsInsert || containsSelect) {
            println(it)
        }
    }

    val bigDecimalAdapter = BigDecimalColumnAdapter
    val dateAdapter = DateColumnAdapter
    val timeAdapter = TimeColumnAdapter

    TeacherDatabase.Schema.create(driver)

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