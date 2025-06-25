package com.jesushz.notemarkmilestone.note.domain.repository

import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.EmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.domain.note.NoteId
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotesLocalSync(): Flow<List<Note>>
    suspend fun getNotesRemoteSync(
        page: Int?,
        pageSize: Int?
    ): Result<List<Note>, DataError.Network>
    suspend fun upsertNote(note: Note, isUpdate: Boolean): EmptyDataResult<DataError>
    suspend fun deleteNote(id: NoteId)
    suspend fun deleteAllNotes()
    suspend fun syncPendingNotes(): EmptyDataResult<DataError.Network>

}
