package com.jesushz.notemarkmilestone.note.data

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.await
import com.jesushz.notemarkmilestone.core.database.dao.NotePendingSyncDao
import com.jesushz.notemarkmilestone.core.database.entity.DeletedNoteSyncEntity
import com.jesushz.notemarkmilestone.core.database.entity.NotePendingSyncEntity
import com.jesushz.notemarkmilestone.core.database.mappers.toNoteEntity
import com.jesushz.notemarkmilestone.core.domain.auth.SessionStorage
import com.jesushz.notemarkmilestone.core.domain.note.Note
import com.jesushz.notemarkmilestone.core.domain.note.NoteId
import com.jesushz.notemarkmilestone.note.data.scheduler.DeleteNoteWorker
import com.jesushz.notemarkmilestone.note.data.scheduler.FetchNotesWorker
import com.jesushz.notemarkmilestone.note.data.scheduler.UpsertNoteWorker
import com.jesushz.notemarkmilestone.note.domain.SyncNoteScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SyncNoteWorkerScheduler(
    context: Context,
    private val notePendingDao: NotePendingSyncDao,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope
): SyncNoteScheduler {

    private val workManager = WorkManager.Companion.getInstance(context)

    override suspend fun scheduleSync(type: SyncNoteScheduler.SyncType) {
        when (type) {
            is SyncNoteScheduler.SyncType.DeleteNote -> {
                scheduleDeleteNoteWorker(type.noteId)
            }
            is SyncNoteScheduler.SyncType.FetchNotes -> {
                scheduleFetchNotesWorker(type.interval)
            }
            is SyncNoteScheduler.SyncType.UpsertNote -> {
                scheduleUpsertNoteWorker(
                    note = type.note,
                    isUpdate = type.isUpdate
                )
            }
        }
    }

    private suspend fun scheduleUpsertNoteWorker(note: Note, isUpdate: Boolean) {
        val userId = sessionStorage.get()?.username ?: return
        val pendingNote = NotePendingSyncEntity(
            note = note.toNoteEntity(),
            userId = userId,
            isUpdate = isUpdate
        )
        notePendingDao.upsertNotePendingSyncEntity(pendingNote)

        val workRequest = OneTimeWorkRequestBuilder<UpsertNoteWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(UpsertNoteWorker.RUN_ID, pendingNote.noteId)
                    .putBoolean(UpsertNoteWorker.IS_UPDATE, isUpdate)
                    .build()
            )
            .addTag("upsert_work")
            .build()

        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleFetchNotesWorker(interval: Duration) {
        val isSyncScheduled = withContext(Dispatchers.IO) {
            workManager
                .getWorkInfosByTag("sync_work")
                .get()
                .isNotEmpty()
        }
        if (isSyncScheduled) return

        val workRequest = PeriodicWorkRequestBuilder<FetchNotesWorker>(
            repeatInterval = interval.toJavaDuration()
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInitialDelay(
                duration = 30,
                timeUnit = TimeUnit.MINUTES
            )
            .addTag("sync_work")
            .build()
        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

    private suspend fun scheduleDeleteNoteWorker(noteId: NoteId) {
        val userId = sessionStorage.get()?.username ?: return
        val entity = DeletedNoteSyncEntity(
            noteId = noteId,
            userId = userId
        )
        notePendingDao.upsertDeletedNoteSyncEntity(entity)

        val workRequest = OneTimeWorkRequestBuilder<DeleteNoteWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.EXPONENTIAL,
                backoffDelay = 2000L,
                timeUnit = TimeUnit.MILLISECONDS
            )
            .setInputData(
                Data.Builder()
                    .putString(DeleteNoteWorker.NOTE_ID, entity.noteId)
                    .build()
            )
            .addTag("delete_work")
            .build()
        applicationScope.launch {
            workManager.enqueue(workRequest).await()
        }.join()
    }

}