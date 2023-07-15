package com.example.teacherapp.core.database.datasource.utils.querymapper

import com.example.teacherapp.core.database.generated.queries.grade.GetGradesByGradeTemplateId
import com.example.teacherapp.core.model.data.BasicGradeForTemplate

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