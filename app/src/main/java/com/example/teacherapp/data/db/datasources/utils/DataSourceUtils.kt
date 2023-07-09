package com.example.teacherapp.data.db.datasources.utils

import com.example.teacherapp.data.db.queries.CommonQueries

fun CommonQueries.insertAndGetIdOrNull(block: () -> Unit): Long? {
    block()
    return this.lastInsertedRowId().executeAsOneOrNull()
}

fun CommonQueries.insertAndGetId(block: () -> Unit): Long {
    block()
    return this.lastInsertedRowId().executeAsOne()
}