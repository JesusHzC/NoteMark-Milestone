package com.jesushz.notemarkmilestone.note.data.scheduler

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jesushz.notemarkmilestone.note.data.mappers.toWorkerResult
import com.jesushz.notemarkmilestone.note.domain.repository.NoteRepository

class FetchNotesWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: NoteRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }
        return when (val result = repository.getNotesRemoteSync(null, null)) {
            is com.jesushz.notemarkmilestone.core.domain.networking.Result.Error -> {
                result.error.toWorkerResult()
            }
            is com.jesushz.notemarkmilestone.core.domain.networking.Result.Success -> {
                Result.success()
            }
        }
    }

}
