package com.example.teacherapp.core.common.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object TimeUtils {

    fun currentDate(): LocalDate = LocalDate.now()

    fun currentTime(): LocalTime = LocalTime.now()

    fun dateToMillis(localDate: LocalDate): Long =
        localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

    fun millisToDate(epochMilli: Long): LocalDate = Instant
        .ofEpochMilli(epochMilli)
        .atZone(ZoneId.of("UTC"))
        .toLocalDate()

    fun localDateYear(date: LocalDate): Int = date.year

    fun localTimeOf(hour: Int, minute: Int): LocalTime = LocalTime.of(hour, minute)

    fun localTimeHour(localTime: LocalTime): Int = localTime.hour

    fun localTimeMinute(localTime: LocalTime): Int = localTime.minute

    fun decodeLocalDate(text: CharSequence): LocalDate =
        LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE)

    fun encodeLocalDate(localDate: LocalDate): String =
        localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)

    fun decodeLocalTime(text: CharSequence): LocalTime =
        LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME)

    fun encodeLocalTime(localTime: LocalTime): String =
        localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)

    fun plusDays(date: LocalDate, daysToAdd: Long): LocalDate = date.plusDays(daysToAdd)

    fun plusTime(time: LocalTime, hours: Long, minutes: Long): LocalTime =
        time.plusHours(hours).plusMinutes(minutes)

    fun format(localDate: LocalDate): String = localDate.format(dateFormat)

    fun format(localTime: LocalTime): String = localTime.format(timeFormat)

    fun getDisplayNameOfDayOfWeek(date: LocalDate): String =
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

    private val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
}