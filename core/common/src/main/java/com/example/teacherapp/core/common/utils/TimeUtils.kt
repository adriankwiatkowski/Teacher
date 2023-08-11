package com.example.teacherapp.core.common.utils

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
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

    fun minusDays(date: LocalDate, daysToSubtract: Long): LocalDate = date.minusDays(daysToSubtract)

    fun plusTime(time: LocalTime, hours: Long, minutes: Long): LocalTime =
        time.plusHours(hours).plusMinutes(minutes)

    fun minusTime(time: LocalTime, hours: Long, minutes: Long): LocalTime =
        time.minusHours(hours).minusMinutes(minutes)

    fun format(localDate: LocalDate): String = localDate.format(dateFormat)

    fun format(localTime: LocalTime): String = localTime.format(timeFormat)

    fun format(a: LocalTime, b: LocalTime): String =
        "${a.format(timeFormat)} - ${b.format(timeFormat)}"

    fun getDisplayNameOfDayOfWeek(date: LocalDate): String =
        date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

    fun getDisplayNameOfDayOfWeek(day: DayOfWeek): String =
        day.getDisplayName(TextStyle.FULL, Locale.getDefault())

    fun isBefore(a: LocalTime, b: LocalTime): Boolean = a.isBefore(b)

    fun isAfter(a: LocalTime, b: LocalTime): Boolean = a.isAfter(b)

    fun isBetween(date: LocalDate, start: LocalDate, end: LocalDate): Boolean =
        !date.isBefore(start) && !date.isAfter(end)

    fun days(): Array<DayOfWeek> = arrayOf(
        monday(),
        tuesday(),
        wednesday(),
        thursday(),
        friday(),
        saturday(),
        sunday(),
    )

    fun monday(): DayOfWeek = DayOfWeek.MONDAY

    fun tuesday(): DayOfWeek = DayOfWeek.TUESDAY

    fun wednesday(): DayOfWeek = DayOfWeek.WEDNESDAY

    fun thursday(): DayOfWeek = DayOfWeek.THURSDAY

    fun friday(): DayOfWeek = DayOfWeek.FRIDAY

    fun saturday(): DayOfWeek = DayOfWeek.SATURDAY

    fun sunday(): DayOfWeek = DayOfWeek.SUNDAY

    fun firstDayOfWeekFromDate(date: LocalDate, day: DayOfWeek): LocalDate {
        var newDate = date
        while (newDate.dayOfWeek != day) {
            newDate = newDate.plusDays(1)
        }
        return newDate
    }

    private val dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
}