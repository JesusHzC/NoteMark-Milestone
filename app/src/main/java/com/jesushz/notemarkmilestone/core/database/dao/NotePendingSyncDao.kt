package com.jesushz.notemarkmilestone.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jesushz.notemarkmilestone.core.database.entity.DeletedNoteSyncEntity
import com.jesushz.notemarkmilestone.core.database.entity.NotePendingSyncEntity

@Dao
interface NotePendingSyncDao {

    @Query("""
        SELECT * FROM notependingsyncentity WHERE userId = :userId
    """)
    suspend fun getAllNotePendingSyncEntities(
        userId: String
    ): List<NotePendingSyncEntity>

    @Query("""
        SELECT * FROM notependingsyncentity WHERE noteId = :noteId
    """)
    suspend fun getNotePendingSyncEntity(
        noteId: String
    ): NotePendingSyncEntity?

    @Upsert
    suspend fun upsertNotePendingSyncEntity(entity: NotePendingSyncEntity)

    @Query("""
        DELETE FROM notependingsyncentity WHERE noteId = :noteId
    """)
    suspend fun deleteNotePendingSyncEntity(noteId: String)

    @Query("""
        DELETE FROM notependingsyncentity WHERE userId = :userId
    """)
    suspend fun deleteAllNotePendingSyncEntities(userId: String)

    // DELETED NOTES
    @Query("""
        SELECT * FROM deletednotesyncentity WHERE userId = :userId
    """)
    suspend fun getAllDeletedNoteSyncEntities(
        userId: String
    ): List<DeletedNoteSyncEntity>

    @Upsert
    suspend fun upsertDeletedNoteSyncEntity(entity: DeletedNoteSyncEntity)

    @Query("""
        DELETE FROM deletednotesyncentity WHERE noteId = :noteId
    """)
    suspend fun deleteDeletedNoteSyncEntity(noteId: String)

}