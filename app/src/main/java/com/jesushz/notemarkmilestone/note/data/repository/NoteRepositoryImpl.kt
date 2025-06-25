package com.jesushz.notemarkmilestone.note.data.repository

import com.jesushz.notemarkmilestone.core.database.dao.NotePendingSyncDao
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.EmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.networking.asEmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.note.LocalNoteDataSource
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.domain.note.NoteId
import com.jesushz.notemarkmilestone.note.domain.RemoteNoteDataSource
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val applicationScope: CoroutineScope,
    private val remoteDataSource: RemoteNoteDataSource,
    private val localDataSource: LocalNoteDataSource,
    private val notePendingSyncDao: NotePendingSyncDao
): NoteRepository {

    override fun getNotesLocalSync(): Flow<List<Note>> {
        return localDataSource.getNotes()
    }

    override suspend fun getNotesRemoteSync(
        page: Int,
        pageSize: Int
    ): Result<List<Note>, DataError.Network> {
        val result = remoteDataSource.getNotes(page, pageSize)
        return when (result) {
            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertNotes(result.data).asEmptyDataResult()
                }.await()
                Result.Success(result.data)
            }
            is Result.Error -> {
                result
            }
        }
    }

    override suspend fun upsertNote(note: Note): EmptyDataResult<DataError> {
        val localResult = localDataSource.upsertNote(note)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val noteWithId = note.copy(id = localResult.data)
        val remoteResult = remoteDataSource.postNote(
            note = noteWithId
        )

        return when (remoteResult) {
            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertNote(remoteResult.data).asEmptyDataResult()
                }.await()
            }
            is Result.Error -> {
                // TODO: SCHEDULE SYNC
                Result.Success(Unit)
            }
        }
    }

    override suspend fun deleteNote(id: NoteId) {
        localDataSource.deleteNote(id)

        val isPendingSync = notePendingSyncDao.getNotePendingSyncEntity(id) != null
        if (isPendingSync) {
            notePendingSyncDao.deleteNotePendingSyncEntity(id)
            return
        }

        val remoteResult = applicationScope.async {
            remoteDataSource.deleteNote(id)
        }.await()
        if (remoteResult is Result.Error) {
            // TODO: SCHEDULE DELETE SYNC
        }
    }

    override suspend fun deleteAllNotes() {
        localDataSource.deleteAllNotes()
    }

    override suspend fun syncPendingNotes(): EmptyDataResult<DataError.Network> {
        // TODO("Not yet implemented")
        return Result.Success(Unit)
    }

}
