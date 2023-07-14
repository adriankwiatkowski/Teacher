package com.example.teacherapp.core.database.datasource.utils

import com.example.teacherapp.core.database.generated.queries.CommonQueries

internal fun CommonQueries.insertAndGetIdOrNull(block: () -> Unit): Long? {
    block()
    return this.lastInsertedRowId().executeAsOneOrNull()
}

internal fun CommonQueries.insertAndGetId(block: () -> Unit): Long {
    block()
    return this.lastInsertedRowId().executeAsOne()
}