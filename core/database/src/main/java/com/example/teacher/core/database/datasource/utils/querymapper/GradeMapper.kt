package com.example.teacher.core.database.datasource.utils.querymapper

import com.example.teacher.core.database.generated.queries.grade.GetGradeById
import com.example.teacher.core.database.generated.queries.grade.GetGradeTemplateInfoByGradeTemplateId
import com.example.teacher.core.database.generated.queries.grade.GetGradesByGradeTemplateId
import com.example.teacher.core.model.data.BasicGradeForTemplate
import com.example.teacher.core.model.data.BasicSchoolClass
import com.example.teacher.core.model.data.Grade
import com.example.teacher.core.model.data.GradeTemplate
import com.example.teacher.core.model.data.GradeTemplateInfo
import com.example.teacher.core.model.data.Lesson
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term

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
            isFirstTerm = grade.grade_is_first_term,
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
        isFirstTerm = grade.grade_is_first_term,
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
        gradeDescription = gradeInfo.grade_template_description,
        gradeWeight = gradeInfo.grade_weight.toInt(),
        isFirstTerm = gradeInfo.grade_is_first_term,
        lesson = Lesson(
            id = gradeInfo.lesson_id,
            name = gradeInfo.lesson_name,
            schoolClass = BasicSchoolClass(
                id = gradeInfo.school_class_id,
                name = gradeInfo.school_class_name,
                schoolYear = SchoolYear(
                    id = gradeInfo.school_year_id,
                    name = gradeInfo.school_year_name,
                    firstTerm = Term(
                        id = gradeInfo.term_first_id,
                        name = gradeInfo.term_first_name,
                        startDate = gradeInfo.term_first_start_date,
                        endDate = gradeInfo.term_first_end_date,
                    ),
                    secondTerm = Term(
                        id = gradeInfo.term_second_id,
                        name = gradeInfo.term_second_name,
                        startDate = gradeInfo.term_second_start_date,
                        endDate = gradeInfo.term_second_end_date,
                    ),
                ),
                studentCount = gradeInfo.student_count,
                lessonCount = gradeInfo.lesson_count,
            ),
        )
    )
}