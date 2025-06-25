package com.jesushz.notemarkmilestone.note.data

import com.jesushz.notemarkmilestone.core.data.networking.delete
import com.jesushz.notemarkmilestone.core.data.networking.get
import com.jesushz.notemarkmilestone.core.data.networking.post
import com.jesushz.notemarkmilestone.core.data.networking.put
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.EmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.networking.map
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.util.Constants.ENDPOINT_NOTES
import com.jesushz.notemarkmilestone.note.data.mappers.toNote
import com.jesushz.notemarkmilestone.note.data.mappers.toNoteSerializable
import com.jesushz.notemarkmilestone.note.data.model.NoteSerializable
import com.jesushz.notemarkmilestone.note.data.model.NotesResponse
import com.jesushz.notemarkmilestone.note.domain.RemoteNoteDataSource
import io.ktor.client.HttpClient
import kotlin.collections.map

class KtorRemoteNoteDataSource(
    private val httpClient: HttpClient
): RemoteNoteDataSource {

    override suspend fun getNotes(
        page: Int?,
        pageSize: Int?
    ): Result<List<Note>, DataError.Network> {
        return httpClient.get<NotesResponse>(
            route = ENDPOINT_NOTES,
            queryParameters = if (page != null && pageSize != null) {
                mapOf(
                    "page" to page,
                    "size" to pageSize
                )
            } else {
                emptyMap()
            }
        ).map { response ->
            response.notes.map { it.toNote() }
        }
    }

    override suspend fun postNote(note: Note): Result<Note, DataError.Network> {
        return httpClient.post<NoteSerializable, NoteSerializable>(
            route = ENDPOINT_NOTES,
            body = note.toNoteSerializable()
        ).map { it.toNote() }
    }

    override suspend fun putNote(note: Note): Result<Note, DataError.Network> {
        return httpClient.put<NoteSerializable, NoteSerializable>(
            route = ENDPOINT_NOTES,
            body = note.toNoteSerializable()
        ).map { it.toNote() }
    }

    override suspend fun deleteNote(id: String): EmptyDataResult<DataError.Network> {
        return httpClient.delete(
            route = "$ENDPOINT_NOTES/$id",
        )
    }
}