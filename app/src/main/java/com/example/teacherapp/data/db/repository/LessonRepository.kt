package com.example.teacherapp.data.db.repository

import android.database.sqlite.SQLiteException
import com.example.teacherapp.data.db.datasources.lesson.LessonDataSource
import com.example.teacherapp.data.di.ApplicationScope
import com.example.teacherapp.data.di.DefaultDispatcher
import com.example.teacherapp.data.models.Resource
import com.example.teacherapp.data.models.entities.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class LessonRepository @Inject constructor(
    private val dataSource: LessonDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) {

    fun getLessonById(lessonId: Long): Flow<Resource<Lesson?>> = flow {
        if (lessonId == 0L) {
            return@flow emit(Resource.Success(null))
        }

        dataSource
            .getLessonById(lessonId)
            .catch { e ->
                if (e !is SQLiteException) {
                    throw e
                }

                emit(Resource.Error(NoSuchElementException()))
            }
            .onStart { emit(Resource.Loading) }
            .collect { lesson -> emit(Resource.Success(lesson)) }
    }

    fun getSchoolClassNameById(schoolClassId: Long): Flow<String?> =
        dataSource.getStudentSchoolClassNameById(schoolClassId)

    suspend fun insertOrUpdateLesson(
        id: Long?,
        schoolClassId: Long,
        name: String,
    ): Boolean {
        return withContext(scope.coroutineContext + dispatcher) {
            try {
                dataSource.insertOrUpdateLesson(
                    id = id,
                    schoolClassId = schoolClassId,
                    name = name,
                )
                true
            } catch (e: Exception) {
                if (e is CancellationException) {
                    throw e
                }
                false
            }
        }
    }
}