package com.example.teacher.core.database.querymapper

import com.example.teacher.core.common.utils.DecimalUtils
import com.example.teacher.core.database.generated.queries.gradetemplate.GetGradeTemplateById
import com.example.teacher.core.database.generated.queries.gradetemplate.GetGradeTemplatesByLessonId
import com.example.teacher.core.model.data.BasicGradeTemplate
import com.example.teacher.core.model.data.GradeTemplate
import com.example.teacher.core.model.data.GradeWithWeight

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
        isFirstTerm = grade.grade_is_first_term,
    )
}

internal fun toExternal(
    gradeTemplates: List<GetGradeTemplatesByLessonId>
): List<BasicGradeTemplate> = gradeTemplates
    .groupBy { gradeTemplate -> gradeTemplate.id }
    .map { (_, gradeTemplateByStudent) ->
        val gradeTemplate = gradeTemplateByStudent.first()

        val studentGrades = gradeTemplateByStudent.mapNotNull { studentGrade ->
            studentGrade.grade?.let { grade ->
                GradeWithWeight(grade, gradeTemplate.weight.toInt())
            }
        }
        val averageGrade = DecimalUtils.calculateWeightedAverage(studentGrades)

        BasicGradeTemplate(
            id = gradeTemplate.id,
            lessonId = gradeTemplate.lesson_id,
            name = gradeTemplate.name,
            weight = gradeTemplate.weight.toInt(),
            isFirstTerm = gradeTemplate.grade_is_first_term,
            averageGrade = averageGrade,
        )
    }

//internal fun toExternal(
//    gradeTemplates: List<GetGradeTemplatesByLessonId>
//): List<BasicGradeTemplate> = gradeTemplates.map { grade ->
//    BasicGradeTemplate(
//        id = grade.id,
//        lessonId = grade.lesson_id,
//        name = grade.name,
//        weight = grade.weight.toInt(),
//        isFirstTerm = grade.grade_is_first_term,
//        averageGrade = null,
//    )
//}