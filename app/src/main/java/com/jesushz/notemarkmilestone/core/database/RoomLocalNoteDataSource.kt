package com.jesushz.notemarkmilestone.core.database

import android.database.sqlite.SQLiteFullException
import com.jesushz.notemarkmilestone.core.database.dao.NoteDao
import com.jesushz.notemarkmilestone.core.database.mappers.toNote
import com.jesushz.notemarkmilestone.core.database.mappers.toNoteEntity
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.note.LocalNoteDataSource
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.domain.note.NoteId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalNoteDataSource(
    private val noteDao: NoteDao
): LocalNoteDataSource {

    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
            .map { noteList ->
                noteList.map { it.toNote() }
            }
    }

    override suspend fun upsertNote(note: Note): Result<NoteId, DataError.Local> {
        return try {
            val entity = note.toNoteEntity()
            noteDao.upsertNote(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertNotes(notes: List<Note>): Result<List<NoteId>, DataError.Local> {
        return try {
            val entities = notes.map { it.toNoteEntity() }
            noteDao.upsertNotes(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteNote(id: String) {
        noteDao.deleteNote(id)
    }

    override suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }

}
