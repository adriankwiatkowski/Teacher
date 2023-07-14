package com.example.teacherapp.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    object Loading : Result<Nothing>
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

fun <T> Flow<T>.asResultNotNull(): Flow<Result<T & Any>> {
    return this
        .map(::mapNotNull)
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}

fun <T> Flow<Result<T>>.notNull(): Flow<Result<T & Any>> {
    return this
        .map { result ->
            when (result) {
                is Result.Success -> result.mapNotNull()
                is Result.Error -> Result.Error(result.exception)
                Result.Loading -> Result.Loading
            }
        }
}

private fun <T> Result.Success<T>.mapNotNull(): Result<T & Any> {
    return mapNotNull(this.data)
}

private fun <T> mapNotNull(data: T?): Result<T & Any> {
    return if (data != null) Result.Success(data) else Result.Error(NullPointerException())
}