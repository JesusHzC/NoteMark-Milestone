package com.jesushz.notemarkmilestone.note.presentation.note_list

import com.jesushz.notemarkmilestone.core.domain.note.Note

data class NoteListState(
    val user: String = "",
    val notes: List<Note> = emptyList(),
    val page: Int = 0,
    val size: Int = 10,
    val isLoading: Boolean = false,
    val endReached: Boolean = false,
) {
    val userInitials: String
        get() = calculateInitials().uppercase()

    private fun calculateInitials(): String {
        if (user.isBlank()) return ""

        val parts = user.trim()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }

        return when {
            user.length == 1 -> user
            parts.size == 1 -> parts.first().take(2)
            parts.size >= 2 -> parts.first().take(1) + parts.last().take(1)
            else -> ""
        }
    }
}
