package com.example.teacherapp.data.db.datasources.utils.querymappers

import com.example.teacherapp.data.models.entities.BasicStudent

object StudentMapper {

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