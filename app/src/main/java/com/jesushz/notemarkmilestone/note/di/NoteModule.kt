package com.jesushz.notemarkmilestone.note.di

import com.jesushz.notemarkmilestone.note.data.repository.NoteRepositoryImpl
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import com.jesushz.notemarkmilestone.note.presentation.note_list.NoteListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val noteModule = module {
    viewModelOf(::NoteListViewModel)

    singleOf(::NoteRepositoryImpl).bind<NoteRepository>()
}
