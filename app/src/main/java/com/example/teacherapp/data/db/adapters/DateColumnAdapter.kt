package com.example.teacherapp.data.db.adapters

import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateColumnAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return LocalDate.parse(databaseValue, DateTimeFormatter.ISO_LOCAL_DATE)
    }

    override fun encode(value: LocalDate): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}