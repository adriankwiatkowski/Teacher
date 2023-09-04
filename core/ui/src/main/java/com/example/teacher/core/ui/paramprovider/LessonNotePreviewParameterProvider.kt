package com.example.teacher.core.ui.paramprovider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.teacher.core.model.data.BasicLesson
import com.example.teacher.core.model.data.BasicLessonNote
import com.example.teacher.core.model.data.LessonNote

class LessonNotesPreviewParameterProvider : PreviewParameterProvider<List<BasicLessonNote>> {
    override val values: Sequence<List<BasicLessonNote>> = sequenceOf(listOf(*basicNotes))
}

class LessonNotePreviewParameterProvider : PreviewParameterProvider<LessonNote> {
    override val values: Sequence<LessonNote> = notes.asSequence()
}

private val basicNotes = makeNotes(
    "Shop list" to "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc vitae felis porta, placerat lorem eu, commodo sem. Aliquam vitae augue eu metus accumsan sodales sit amet sit amet nunc. Nulla quis ante quis ligula posuere finibus vel sed risus. Nulla aliquet ligula est, vel porttitor lacus mattis eget. Fusce eu libero vel nibh sagittis tincidunt quis eget mauris. Etiam eu tincidunt dolor. Proin gravida, lacus et ultrices imperdiet, ante ligula vehicula metus, nec posuere orci risus tincidunt eros. Aliquam erat volutpat. Vivamus vulputate diam turpis, non facilisis lorem mattis at. Nulla facilisi. Mauris ut ante molestie, mollis odio ac, imperdiet nunc. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; In in metus eget massa interdum dapibus efficitur in est. Fusce eleifend consequat nisi ut tristique.",
    "Lunch" to "Etiam ac euismod mauris, eu accumsan leo. Maecenas tincidunt viverra risus eu ultricies. Phasellus vitae enim fermentum, varius ipsum ac, congue nisi. Pellentesque in lacus arcu. Aliquam euismod consequat laoreet. Praesent auctor dictum ex ut rhoncus. Nunc in eros ut sem suscipit gravida. Suspendisse ut neque ac ligula iaculis efficitur. In et lorem fermentum, vehicula leo eget, rhoncus lacus. Sed nulla lectus, rhoncus nec condimentum in, euismod accumsan erat. Etiam ut iaculis felis. Vestibulum rutrum massa sit amet libero placerat, eu dapibus nisl lobortis. Phasellus mollis odio dui, vitae accumsan augue tempor sed. Ut pellentesque turpis quis accumsan consectetur. Etiam tincidunt nisi nec tincidunt faucibus. In viverra, nulla sit amet mattis finibus, lacus eros gravida orci, at dapibus nisl nisl vitae orci.",
    "Bookmarks" to "Fusce sed quam porttitor, viverra urna ut, lacinia elit. Aliquam viverra lorem elit, id ultrices urna suscipit eget. Cras finibus lectus non ante dictum dapibus quis eget eros. Suspendisse aliquam elementum ornare. Aenean faucibus, lorem sed lacinia luctus, est elit elementum nibh, vel aliquam magna turpis eu orci. Integer id tortor non quam ultrices tristique suscipit vel tortor. Phasellus eget elit dui. Nulla vel congue odio. Duis sagittis ornare fermentum. Nam interdum ornare mauris, sit amet mattis libero dictum sit amet. In vel dolor ligula. Vivamus suscipit ornare mi auctor semper. Integer sed ante vel tellus rhoncus pulvinar.",
    "School" to "Nulla facilisi. In non odio tempor, fermentum nisi eget, aliquam lacus. Nulla facilisi. Ut sed magna venenatis, aliquet metus quis, facilisis lacus. Nunc fermentum dui at purus sodales tincidunt. Vestibulum sagittis consequat ornare. Proin vehicula at nisl non hendrerit. Integer sollicitudin urna orci, et lobortis neque egestas id. Morbi eget accumsan velit, et lacinia lectus. Nulla non sapien mauris. Ut laoreet, mi id facilisis efficitur, risus urna consequat nisi, et porta erat tellus eget mi. Ut ut pharetra neque. Nullam eget ipsum pharetra, tempus mauris non, luctus risus. Donec sollicitudin purus ut ante tempor malesuada.",
    "Note" to "Pellentesque luctus sit amet magna eget hendrerit. Duis dignissim mauris vitae elit ullamcorper, in dictum nulla viverra. Pellentesque dolor elit, condimentum ac nibh ac, suscipit ultricies lorem. Nunc rutrum vel magna in ullamcorper. Nulla lobortis, lacus eget ullamcorper vehicula, lectus eros mollis nibh, vitae pulvinar felis sem non leo. Ut ut tortor urna. Sed quis pellentesque risus. Integer arcu orci, auctor in ante et, maximus posuere felis. Aenean volutpat at lectus ut interdum. Nunc diam neque, pellentesque in finibus sit amet, lobortis in sapien. Nullam luctus lectus vel dapibus tristique. Nam finibus volutpat diam. Phasellus sit amet ipsum cursus tortor vehicula placerat nec sit amet sem.",
    "Note from day 01.09.2014" to "Ut nibh quam, lacinia aliquet nisi eu, posuere pharetra libero. Duis ullamcorper urna sed mauris cursus, ac dignissim mauris feugiat. Morbi a blandit sapien. Donec ac hendrerit metus, non condimentum libero. Vestibulum vel aliquam lorem. Proin in sem quis purus porttitor mattis. Fusce mollis cursus eros, sit amet pellentesque eros sollicitudin et. Sed iaculis ligula felis, nec mattis tellus fermentum at. Quisque efficitur leo et velit dapibus, quis semper massa dignissim. Praesent sagittis ornare lectus sed laoreet. Nullam in nisi sodales, euismod nisl id, facilisis leo. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Quisque luctus tempor risus, in eleifend ligula tempor a. Maecenas ac quam interdum, porttitor nisi at, euismod metus. Vestibulum lectus orci, aliquam at elit in, feugiat placerat tortor.",
)

private val notes = basicNotes.map { note ->
    LessonNote(
        id = note.id,
        title = note.title,
        text = note.text,
        lesson = BasicLesson(
            id = note.lessonId,
            name = "Matematyka",
            schoolClassId = 1L,
        )
    )
}

private fun makeNotes(vararg data: Pair<String, String>): Array<BasicLessonNote> =
    data.mapIndexed { index, (title, text) ->
        makeNote(
            id = index.toLong(),
            title = title,
            text = text,
            lessonId = 1L,
        )
    }.toTypedArray()

private fun makeNote(id: Long, title: String, text: String, lessonId: Long): BasicLessonNote {
    return BasicLessonNote(
        id = id,
        title = title,
        text = text,
        lessonId = lessonId,
    )
}