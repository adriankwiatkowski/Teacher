package com.example.teacherapp.core.database.adapter

import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal object DateColumnAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    override fun encode(value: LocalDate): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}