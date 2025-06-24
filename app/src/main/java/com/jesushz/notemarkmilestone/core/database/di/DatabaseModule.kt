package com.jesushz.notemarkmilestone.core.database.di

import androidx.room.Room
import com.jesushz.notemarkmilestone.core.database.NoteMarkDatabase
import com.jesushz.notemarkmilestone.core.database.RoomLocalNoteDataSource
import com.jesushz.notemarkmilestone.core.domain.note.LocalNoteDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            NoteMarkDatabase::class.java,
            NoteMarkDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration(false).build()
    }
    single { get<NoteMarkDatabase>().noteDao }
    single { get<NoteMarkDatabase>().notePendingDao }
    singleOf(::RoomLocalNoteDataSource).bind<LocalNoteDataSource>()
}