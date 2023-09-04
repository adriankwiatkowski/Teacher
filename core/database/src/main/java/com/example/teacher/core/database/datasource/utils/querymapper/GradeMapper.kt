package com.example.teacher.core.database.datasource.utils.querymapper

import com.example.teacher.core.database.generated.queries.grade.GetGradeById
import com.example.teacher.core.database.generated.queries.grade.GetGradeTemplateInfoByGradeTemplateId
import com.example.teacher.core.database.generated.queries.grade.GetGradesByGradeTemplateId
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.Grade
import com.example.teacher.core.model.data.GradeTemplate
import com.example.teacher.core.model.data.GradeTemplateInfo

internal fun toExternal(
    grade: GetGradeById?
): Grade? = run {
    if (grade == null) {
        return@run null
    }

    Grade(
        id = grade.id,
        grade = grade.grade,
        date = grade.date,
        studentId = grade.student_id,
        studentFullName = "${grade.student_name} ${grade.student_surname}",
        lessonId = grade.lesson_id,
        lessonName = grade.lesson_name,
        schoolClassId = grade.school_class_id,
        schoolClassName = grade.school_class_name,
        gradeTemplate = GradeTemplate(
            id = grade.grade_template_id,
            lessonId = grade.lesson_id,
            name = grade.grade_template_name,
            description = grade.grade_template_description,
            weight = grade.grade_template_weight.toInt(),
        ),
    )
}

internal fun toExternal(
    grades: List<GetGradesByGradeTemplateId>
): List<BasicGradeForTemplate> = grades.map { grade ->
    BasicGradeForTemplate(
        id = grade.id,
        grade = grade.grade,
        date = grade.date,
        studentId = grade.student_id,
        studentFullName = "${grade.student_name} ${grade.student_surname}",
        gradeName = grade.grade_template_name,
        gradeWeight = grade.grade_template_weight.toInt(),
    )
}

internal fun toExternal(
    gradeInfo: GetGradeTemplateInfoByGradeTemplateId?
): GradeTemplateInfo? = run {
    if (gradeInfo == null) {
        return@run null
    }

    GradeTemplateInfo(
        gradeTemplateId = gradeInfo.grade_template_id,
        gradeName = gradeInfo.grade_name,
        gradeWeight = gradeInfo.grade_weight.toInt(),
        lessonId = gradeInfo.lessson_id,
        lessonName = gradeInfo.lesson_name,
        schoolClassId = gradeInfo.school_class_id,
        schoolClassName = gradeInfo.school_class_name,
    )
}