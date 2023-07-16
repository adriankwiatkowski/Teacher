package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.grade.GetGradeTemplateInfoByGradeTemplateId
import com.example.teacherapp.core.database.generated.queries.grade.GetGradesByGradeTemplateId
import com.example.teacherapp.core.model.data.BasicGradeForTemplate
import com.example.teacherapp.core.model.data.GradeTemplateInfo

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