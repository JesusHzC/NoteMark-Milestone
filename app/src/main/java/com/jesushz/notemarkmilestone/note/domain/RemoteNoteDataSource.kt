package com.jesushz.notemarkmilestone.note.domain

import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.EmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.note.Note

interface RemoteNoteDataSource {
    suspend fun getNotes(
        page: Int,
        pageSize: Int
    ): Result<List<Note>, DataError.Network>
    suspend fun postNote(note: Note): Result<Note, DataError.Network>
    suspend fun deleteNote(id: String): EmptyDataResult<DataError.Network>
}
