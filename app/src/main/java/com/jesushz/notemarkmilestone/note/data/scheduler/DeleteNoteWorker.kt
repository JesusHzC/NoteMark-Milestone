package com.jesushz.notemarkmilestone.note.data.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jesushz.notemarkmilestone.core.database.dao.NotePendingSyncDao
import com.jesushz.notemarkmilestone.note.data.mappers.toWorkerResult
import com.jesushz.notemarkmilestone.note.domain.RemoteNoteDataSource

class DeleteNoteWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteNoteDataSource: RemoteNoteDataSource,
    private val notePendingDao: NotePendingSyncDao
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        val noteId = params.inputData.getString(NOTE_ID) ?: return Result.failure()
        return when (val result = remoteNoteDataSource.deleteNote(noteId)) {
            is com.jesushz.notemarkmilestone.core.domain.networking.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.jesushz.notemarkmilestone.core.domain.networking.Result.Success -> {
                notePendingDao.deleteDeletedNoteSyncEntity(noteId)
                Result.success()
            }
        }
    }

    companion object {
        const val NOTE_ID = "noteId"
    }

}
