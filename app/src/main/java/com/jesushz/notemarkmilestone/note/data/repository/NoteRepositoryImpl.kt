package com.jesushz.notemarkmilestone.note.data.repository

import com.jesushz.notemarkmilestone.core.database.dao.NotePendingSyncDao
import com.jesushz.notemarkmilestone.core.database.mappers.toNote
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import com.jesushz.notemarkmilestone.core.domain.networking.DataError
import com.jesushz.notemarkmilestone.core.domain.networking.EmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.networking.Result
import com.jesushz.notemarkmilestone.core.domain.networking.asEmptyDataResult
import com.jesushz.notemarkmilestone.core.domain.note.LocalNoteDataSource
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.domain.note.NoteId
import com.jesushz.notemarkmilestone.note.domain.RemoteNoteDataSource
import com.jesushz.notemarkmilestone.note.domain.SyncNoteScheduler
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteRepositoryImpl(
    private val applicationScope: CoroutineScope,
    private val remoteDataSource: RemoteNoteDataSource,
    private val localDataSource: LocalNoteDataSource,
    private val notePendingSyncDao: NotePendingSyncDao,
    private val syncNoteScheduler: SyncNoteScheduler,
    private val sessionStorage: SessionStorage
): NoteRepository {

    override fun getNotesLocalSync(): Flow<List<Note>> {
        return localDataSource.getNotes()
    }

    override suspend fun getNotesRemoteSync(
        page: Int?,
        pageSize: Int?
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

    override suspend fun upsertNote(note: Note, isUpdate: Boolean): EmptyDataResult<DataError> {
        val localResult = localDataSource.upsertNote(note)
        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val noteWithId = note.copy(id = localResult.data)
        val remoteResult = if (isUpdate) {
            remoteDataSource.putNote(
                note = noteWithId
            )
        } else {
            remoteDataSource.postNote(
                note = noteWithId
            )
        }

        return when (remoteResult) {
            is Result.Success -> {
                applicationScope.async {
                    localDataSource.upsertNote(remoteResult.data).asEmptyDataResult()
                }.await()
            }
            is Result.Error -> {
                applicationScope.launch {
                    syncNoteScheduler.scheduleSync(
                        type = SyncNoteScheduler.SyncType.UpsertNote(
                            note = noteWithId,
                            isUpdate = isUpdate
                        )
                    )
                }.join()
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
            applicationScope.launch {
                syncNoteScheduler.scheduleSync(
                    type = SyncNoteScheduler.SyncType.DeleteNote(id)
                )
            }.join()
        }
    }

    override suspend fun deleteAllNotes() {
        localDataSource.deleteAllNotes()
    }

    override suspend fun syncPendingNotes() {
        withContext(Dispatchers.IO) {
            val userId = sessionStorage.get()?.username ?: return@withContext

            val createNotes = async {
                notePendingSyncDao.getAllNotePendingSyncEntities(userId)
            }
            val deleteNotes = async {
                notePendingSyncDao.getAllDeletedNoteSyncEntities(userId)
            }

            val createJobs = createNotes
                .await()
                .map {
                    launch {
                        val note = it.note.toNote()
                        val result = if (it.isUpdate) {
                            remoteDataSource.putNote(note)
                        } else {
                            remoteDataSource.postNote(note)
                        }
                        when (result) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    notePendingSyncDao.deleteNotePendingSyncEntity(it.noteId)
                                }.join()
                            }
                        }
                    }
                }

            val deleteJobs = deleteNotes
                .await()
                .map {
                    launch {
                        when (remoteDataSource.deleteNote(it.noteId)) {
                            is Result.Error -> Unit
                            is Result.Success -> {
                                applicationScope.launch {
                                    notePendingSyncDao.deleteDeletedNoteSyncEntity(it.noteId)
                                }.join()
                            }
                        }
                    }
                }

            createJobs.forEach { it.join() }
            deleteJobs.forEach { it.join() }
        }
    }

}
