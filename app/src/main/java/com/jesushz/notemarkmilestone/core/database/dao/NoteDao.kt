package com.jesushz.notemarkmilestone.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jesushz.notemarkmilestone.core.database.entity.NoteEntity
import com.jesushz.notemarkmilestone.core.domain.note.NoteId
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Upsert
    suspend fun upsertNotes(notes: List<NoteEntity>)

    @Query("""
        SELECT * FROM noteentity ORDER BY lastEditedAt DESC
    """)
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM noteentity
    """)
    suspend fun getAllNotes(): List<NoteEntity>

    @Query("""
        DELETE FROM noteentity WHERE id = :id
    """)
    suspend fun deleteNote(id: String)

    @Query("""
        DELETE FROM noteentity WHERE id IN (:ids)
    """)
    suspend fun deleteNotes(ids: List<NoteId>)

    @Query("""
        DELETE FROM noteentity
    """)
    suspend fun deleteAllNotes()

}
