package com.example.teacher.core.database.adapter

import app.cash.sqldelight.ColumnAdapter
import java.math.BigDecimal

internal object BigDecimalColumnAdapter : ColumnAdapter<BigDecimal, String> {
    override fun decode(databaseValue: String): BigDecimal {
        return BigDecimal(databaseValue)
    }

    override fun encode(value: BigDecimal): String {
        return value.toString()
    }
}