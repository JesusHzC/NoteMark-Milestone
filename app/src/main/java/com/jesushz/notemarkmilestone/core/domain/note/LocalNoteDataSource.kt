package com.jesushz.notemarkmilestone.core.domain.note

import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import kotlinx.coroutines.flow.Flow

typealias NoteId = String

interface LocalNoteDataSource {
    fun getNotes(): Flow<List<Note>>
    suspend fun upsertNote(note: Note): Result<NoteId, DataError.Local>
    suspend fun upsertNotes(notes: List<Note>): Result<List<NoteId>, DataError.Local>
    suspend fun deleteNote(id: String)
    suspend fun deleteAllNotes()
}