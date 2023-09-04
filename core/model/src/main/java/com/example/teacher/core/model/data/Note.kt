package com.example.teacher.core.model.data

data class Note(
    val id: Long,
    val title: String,
    val text: String,
    val priority: NotePriority,
)

enum class NotePriority(val priority: Long) {
    Low(1),
    Medium(2),
    High(3);

    companion object {
        fun ofPriority(priority: Long): NotePriority = when (priority) {
            Low.priority -> Low
            Medium.priority -> Medium
            High.priority -> High
            else -> throw IllegalArgumentException()
        }
    }
}