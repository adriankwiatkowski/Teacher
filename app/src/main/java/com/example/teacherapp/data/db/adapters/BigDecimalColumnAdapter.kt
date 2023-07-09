package com.example.teacherapp.data.db.adapters

import com.squareup.sqldelight.ColumnAdapter
import java.math.BigDecimal

object BigDecimalColumnAdapter : ColumnAdapter<BigDecimal, String> {
    override fun decode(databaseValue: String): BigDecimal {
        return BigDecimal(databaseValue)
    }

    override fun encode(value: BigDecimal): String {
        return value.toString()
    }
}