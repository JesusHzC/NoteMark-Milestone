package com.jesushz.notemarkmilestone.note.domain

import com.jesushz.notemarkmilestone.core.domain.note.Note
import kotlin.time.Duration

interface SyncNoteScheduler {

    suspend fun scheduleSync(type: SyncType)

    sealed interface SyncType {
        data class FetchNotes(val interval: Duration) : SyncType
        data class DeleteNote(val noteId: String) : SyncType
        class UpsertNote(val note: Note, val isUpdate: Boolean) : SyncType
    }

}
