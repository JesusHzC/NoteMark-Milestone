package com.jesushz.notemarkmilestone.note.di

import com.jesushz.notemarkmilestone.note.data.repository.NoteRepositoryImpl
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import com.jesushz.notemarkmilestone.note.presentation.note_list.NoteListViewModel
import com.jesushz.notemarkmilestone.note.presentation.upsert_note.UpsertNoteViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val noteModule = module {
    viewModelOf(::NoteListViewModel)
    viewModelOf(::UpsertNoteViewModel)

    singleOf(::NoteRepositoryImpl).bind<NoteRepository>()
}
