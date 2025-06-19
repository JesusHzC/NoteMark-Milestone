package com.jesushz.notemarkmilestone.note.presentation.note_list

data class NoteListState(
    val user: String = "",
    val notes: List<String> = emptyList(),
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
