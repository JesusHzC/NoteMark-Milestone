package com.jesushz.notemarkmilestone.note.data.repository

import com.jesushz.notemarkmilestone.core.data.networking.get
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.networking.map
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.util.Constants.ENDPOINT_GET_NOTES
import com.jesushz.notemarkmilestone.note.data.mappers.toNote
import com.jesushz.notemarkmilestone.note.data.model.NotesResponse
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import io.ktor.client.HttpClient

class NoteRepositoryImpl(
    private val httpClient: HttpClient
): NoteRepository {

    override suspend fun getNotes(
        page: Int,
        pageSize: Int
    ): Result<List<Note>, DataError.Network> {
        return httpClient.get<NotesResponse>(
            route = ENDPOINT_GET_NOTES,
            queryParameters = mapOf(
                "page" to page,
                "size" to pageSize
            )
        ).map {
            it.notes.map { it.toNote() }
        }
    }

}
