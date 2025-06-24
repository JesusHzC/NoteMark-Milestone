@file:OptIn(ExperimentalUuidApi::class)

package com.jesushz.notemarkmilestone.note.data.repository

import com.jesushz.notemarkmilestone.core.data.networking.get
import com.jesushz.notemarkmilestone.core.data.networking.post
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.networking.map
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.util.Constants.ENDPOINT_CREATE_NOTE
import com.jesushz.notemarkmilestone.core.util.Constants.ENDPOINT_GET_NOTES
import com.jesushz.notemarkmilestone.core.util.toISO8601Duration
import com.jesushz.notemarkmilestone.note.data.mappers.toNote
import com.jesushz.notemarkmilestone.note.data.model.NoteSerializable
import com.jesushz.notemarkmilestone.note.data.model.NotesResponse
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import io.ktor.client.HttpClient
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    override suspend fun createNote(
        title: String,
        content: String
    ): Result<Note, DataError.Network> {
        val note = NoteSerializable(
            id = Uuid.random().toString(),
            title = title,
            content = content,
            createdAt = System.currentTimeMillis().toISO8601Duration(),
            lastEditedAt = System.currentTimeMillis().toISO8601Duration()
        )

        return httpClient.post<NoteSerializable, NoteSerializable>(
            route = ENDPOINT_CREATE_NOTE,
            body = note
        ).map { it.toNote() }
    }

}
