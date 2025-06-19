package com.jesushz.notemarkmilestone.note.di

import com.jesushz.notemarkmilestone.note.presentation.note_list.NoteListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val noteModule = module {
    viewModelOf(::NoteListViewModel)
}