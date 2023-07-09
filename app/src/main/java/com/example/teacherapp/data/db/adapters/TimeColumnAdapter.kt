package com.example.teacherapp.data.db.adapters

import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object TimeColumnAdapter : ColumnAdapter<LocalTime, String> {
    override fun decode(databaseValue: String): LocalTime {
        return LocalTime.parse(databaseValue, DateTimeFormatter.ISO_LOCAL_TIME)
    }

    override fun encode(value: LocalTime): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_TIME)
    }
}