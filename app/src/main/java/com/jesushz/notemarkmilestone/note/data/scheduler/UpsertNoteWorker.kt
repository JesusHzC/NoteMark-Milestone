package com.jesushz.notemarkmilestone.note.data.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jesushz.notemarkmilestone.core.database.dao.NotePendingSyncDao
import com.jesushz.notemarkmilestone.core.database.mappers.toNote
import com.jesushz.notemarkmilestone.note.data.mappers.toWorkerResult
import com.jesushz.notemarkmilestone.note.domain.RemoteNoteDataSource

class UpsertNoteWorker(
    context: Context,
    private val params: WorkerParameters,
    private val remoteDataSource: RemoteNoteDataSource,
    private val pendingDao: NotePendingSyncDao
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val pendingNoteId = params.inputData.getString(RUN_ID) ?: return Result.failure()
        val isUpdate = params.inputData.getBoolean(IS_UPDATE, false)

        val pendingNoteEntity = pendingDao.getNotePendingSyncEntity(pendingNoteId)
            ?: return Result.failure()

        val note = pendingNoteEntity.note.toNote()
        val result = if (isUpdate) {
            remoteDataSource.putNote(note)
        } else {
            remoteDataSource.postNote(note)
        }
        return when (result) {
            is com.jesushz.notemarkmilestone.core.domain.networking.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.jesushz.notemarkmilestone.core.domain.networking.Result.Success -> {
                pendingDao.deleteNotePendingSyncEntity(pendingNoteId)
                Result.success()
            }
        }
    }

    companion object {
        const val RUN_ID = "RUN_ID"
        const val IS_UPDATE = "IS_UPDATE"
    }

}