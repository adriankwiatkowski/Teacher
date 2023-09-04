package com.example.teacher.core.data.repository.lesson

import com.example.teacher.core.common.di.ApplicationScope
import com.example.teacher.core.common.di.DefaultDispatcher
import com.example.teacher.core.common.result.Result
import com.example.teacher.core.common.result.asResult
import com.example.teacher.core.common.result.asResultNotNull
import com.example.teacher.core.database.datasource.lesson.LessonDataSource
import com.example.teacher.core.model.data.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

internal class DatabaseLessonRepository @Inject constructor(
    private val dataSource: LessonDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonRepository {

    override fun getLessons(): Flow<Result<List<Lesson>>> = dataSource
        .getLessons()
        .asResult()

    override fun getLessonOrNullById(lessonId: Long): Flow<Result<Lesson?>> = dataSource
        .getLessonById(lessonId)
        .asResult()

    override fun getLessonById(lessonId: Long): Flow<Result<Lesson>> = dataSource
        .getLessonById(lessonId)
        .asResultNotNull()

    override fun getSchoolClassNameById(schoolClassId: Long): Flow<String?> = dataSource
        .getStudentSchoolClassNameById(schoolClassId)

    override suspend fun insertOrUpdateLesson(
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

    override suspend fun deleteLessonById(id: Long) {
        scope.launch {
            dataSource.deleteLessonById(id)
        }
    }
}