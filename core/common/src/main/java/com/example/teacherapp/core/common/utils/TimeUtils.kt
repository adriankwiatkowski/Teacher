package com.example.teacherapp.core.common.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun LocalDate.format(): String = this.format(dateFormat)

private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

fun LocalTime.format(): String = this.format(timeFormat)
