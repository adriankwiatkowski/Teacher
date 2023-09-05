package com.example.teacher.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out T> {
    data object Loading : Result<Nothing>
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

inline fun <T, R> Flow<Result<T>>.mapResult(
    crossinline transform: suspend (value: T) -> Result<R>
): Flow<Result<R>> {
    return this
        .map { result ->
            when (result) {
                is Result.Success -> transform(result.data)
                is Result.Error -> Result.Error(result.exception)
                Result.Loading -> Result.Loading
            }
        }
}

fun <T1, T2, R> Flow<Result<T1>>.combineResult(
    other: Flow<Result<T2>>,
    transform: suspend (a: T1, b: T2) -> Result<R>,
): Flow<Result<R>> = combine(this, other) { a, b ->
    when (a) {
        Result.Loading -> Result.Loading
        is Result.Error -> Result.Error(a.exception)
        is Result.Success -> {
            when (b) {
                Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(b.exception)
                is Result.Success -> transform(a.data, b.data)
            }
        }
    }
}

private fun <T> Result.Success<T>.mapNotNull(): Result<T & Any> {
    return mapNotNull(this.data)
}

private fun <T> mapNotNull(data: T?): Result<T & Any> {
    return if (data != null) Result.Success(data) else Result.Error(NoSuchElementException())
}