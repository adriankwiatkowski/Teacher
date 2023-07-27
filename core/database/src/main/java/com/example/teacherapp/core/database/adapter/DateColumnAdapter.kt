package com.example.teacherapp.core.database.adapter

import com.example.teacherapp.core.common.utils.TimeUtils
import com.squareup.sqldelight.ColumnAdapter
import java.time.LocalDate

internal object DateColumnAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String): LocalDate {
        return TimeUtils.decodeLocalDate(databaseValue)
    }

    override fun encode(value: LocalDate): String {
        return TimeUtils.encodeLocalDate(value)
    }
}