package com.example.teacherapp.core.data.repository.lesson

import com.example.teacherapp.core.common.di.ApplicationScope
import com.example.teacherapp.core.common.di.DefaultDispatcher
import com.example.teacherapp.core.common.result.Result
import com.example.teacherapp.core.common.result.asResult
import com.example.teacherapp.core.database.datasource.lesson.LessonDataSource
import com.example.teacherapp.core.model.data.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class DatabaseLessonRepository @Inject constructor(
    private val dataSource: LessonDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : LessonRepository {

    override fun getLessonById(lessonId: Long): Flow<Result<Lesson?>> = dataSource
        .getLessonById(lessonId)
        .asResult()

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
}