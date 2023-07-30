package com.example.teacherapp.core.database.adapter

import app.cash.sqldelight.ColumnAdapter
import com.example.teacherapp.core.common.utils.TimeUtils
import java.time.LocalTime

internal object TimeColumnAdapter : ColumnAdapter<LocalTime, String> {
    override fun decode(databaseValue: String): LocalTime {
        return TimeUtils.decodeLocalTime(databaseValue)
    }

    override fun encode(value: LocalTime): String {
        return TimeUtils.encodeLocalTime(value)
    }
}