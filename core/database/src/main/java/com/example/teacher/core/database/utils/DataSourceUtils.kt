package com.example.teacher.core.database.utils

import com.example.teacher.core.database.generated.queries.CommonQueries

internal fun CommonQueries.insertAndGetIdOrNull(block: () -> Unit): Long? {
    block()
    return this.lastInsertedRowId().executeAsOneOrNull()
}

internal fun CommonQueries.insertAndGetId(block: () -> Unit): Long {
    block()
    return this.lastInsertedRowId().executeAsOne()
}