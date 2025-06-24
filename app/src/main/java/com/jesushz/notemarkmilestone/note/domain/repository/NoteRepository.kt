package com.jesushz.notemarkmilestone.note.domain.repository

import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.note.Note

interface NoteRepository {

    suspend fun getNotes(
        page: Int,
        pageSize: Int
    ): Result<List<Note>, DataError.Network>

    suspend fun createNote(
        title: String,
        content: String
    ): Result<Note, DataError.Network>

}
