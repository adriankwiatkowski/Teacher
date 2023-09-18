package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.Event
import com.example.teacher.core.model.data.Lesson
import java.time.LocalDate
import java.time.LocalTime

class EventsPreviewParameterProvider : PreviewParameterProvider<List<Event>> {
    override val values: Sequence<List<Event>> = sequenceOf(events)
}

private val events = makeEvents(
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 0),
        endTime = TimeUtils.localTimeOf(8, 45),
    ),
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(8, 50),
        endTime = TimeUtils.localTimeOf(9, 35),
    ),
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(9, 50),
        endTime = TimeUtils.localTimeOf(10, 35),
    ),
    SimpleEvent(
        date = TimeUtils.currentDate(),
        startTime = TimeUtils.localTimeOf(10, 40),
        endTime = TimeUtils.localTimeOf(11, 25),
    ),
)

private fun makeEvents(
    vararg lesson: SimpleEvent,
): List<Event> {
    return lesson.mapIndexed { index, simpleEvent ->
        val isCancelled = index % 2 == 1

        listOf(
            Event(
                id = index.toLong() * 2,
                lesson = Lesson(
                    id = 1L,
                    name = "Matematyka",
                    schoolClass = BasicSchoolClassPreviewParameterProvider().values.first()
                ),
                date = simpleEvent.date,
                startTime = simpleEvent.startTime,
                endTime = simpleEvent.endTime,
                isCancelled = isCancelled,
            ),
            Event(
                id = index.toLong() * 2 + 1,
                lesson = null,
                date = simpleEvent.date,
                startTime = simpleEvent.startTime,
                endTime = simpleEvent.endTime,
                isCancelled = isCancelled,
            )
        )
    }.flatten()
}

private data class SimpleEvent(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)