package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.gradetemplate.GetGradeTemplateById
import com.example.teacherapp.core.database.generated.queries.gradetemplate.GetGradeTemplatesByLessonId
import com.example.teacherapp.core.model.data.BasicGradeTemplate
import com.example.teacherapp.core.model.data.GradeTemplate

internal fun toExternal(
    gradeTemplates: List<GetGradeTemplatesByLessonId>
): List<BasicGradeTemplate> = gradeTemplates.map { grade ->
    BasicGradeTemplate(
        id = grade.id,
        lessonId = grade.lesson_id,
        name = grade.name,
        weight = grade.weight.toInt(),
    )
}

internal fun toExternal(grade: GetGradeTemplateById?): GradeTemplate? = run {
    if (grade == null) {
        return@run null
    }

    GradeTemplate(
        id = grade.id,
        lessonId = grade.lesson_id,
        name = grade.name,
        description = grade.description,
        weight = grade.weight.toInt(),
    )
}