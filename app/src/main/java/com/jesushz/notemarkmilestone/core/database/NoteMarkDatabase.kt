package com.jesushz.notemarkmilestone.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jesushz.notemarkmilestone.core.database.dao.NoteDao
import com.jesushz.notemarkmilestone.core.database.dao.NotePendingSyncDao
import com.jesushz.notemarkmilestone.core.database.entity.DeletedNoteSyncEntity
import com.jesushz.notemarkmilestone.core.database.entity.NoteEntity
import com.jesushz.notemarkmilestone.core.database.entity.NotePendingSyncEntity

@Database(
    entities = [
        NoteEntity::class,
        NotePendingSyncEntity::class,
        DeletedNoteSyncEntity::class
    ],
    version = 2,
)
abstract class NoteMarkDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao
    abstract val notePendingDao: NotePendingSyncDao

    companion object {
        const val DATABASE_NAME = "notemark.db"
    }

}
