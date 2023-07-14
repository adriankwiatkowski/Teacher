package com.example.teacherapp.core.database.datasource.utils.querymappers

import com.example.teacherapp.core.model.data.BasicStudent

internal object StudentMapper {

    fun mapBasicStudent(
        id: Long,
        orderInClass: Long,
        schoolClassId: Long,
        name: String,
        surname: String,
        email: String?,
        phone: String?,
    ): BasicStudent {
        return BasicStudent(
            id = id,
            orderInClass = orderInClass,
            classId = schoolClassId,
            name = name,
            surname = surname,
            email = email,
            phone = phone,
        )
    }
}