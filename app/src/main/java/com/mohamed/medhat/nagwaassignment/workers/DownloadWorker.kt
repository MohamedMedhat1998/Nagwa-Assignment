package com.mohamed.medhat.nagwaassignment.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.mohamed.medhat.nagwaassignment.di.PrefsStateKeeper
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.download.DownloadManager
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.*
import com.mohamed.medhat.nagwaassignment.utils.state.DataItemStateKeeper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val INPUT_DATA_DATA_ITEM = "input-data-item"
const val PROGRESS_DATA_PROGRESS = "progress-progress"
const val PROGRESS_DATA_STATE = "progress-state"
private const val TAG = "DownloadWorker"

/**
 * Responsible for the download operation.
 */
@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val downloadManager: DownloadManager,
    @PrefsStateKeeper private val stateKeeper: DataItemStateKeeper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val dataItemJson = inputData.getString(INPUT_DATA_DATA_ITEM) ?: return Result.failure()
        val dataItem =
            Gson().fromJson(dataItemJson, DataItem::class.java) ?: return Result.failure()
        stateKeeper.updateDataItemState(dataItem, STATE_DOWNLOADING)
        val extension = dataItem.url.takeLast(3)
        return withContext(Dispatchers.IO) {
            try {
                val isSuccessful =
                    downloadManager.download(dataItem.url, "${dataItem.id}.$extension") {
                        Log.d(TAG, "doWork: progress: ${it}%")
                        setProgress(workDataOf(PROGRESS_DATA_PROGRESS to it))
                        // TODO create a notification with the progress.
                    }
                if (isSuccessful) {
                    stateKeeper.updateDataItemState(dataItem, STATE_DOWNLOADED)
                    Result.success()
                } else {
                    stateKeeper.updateDataItemState(dataItem, STATE_NOT_DOWNLOADED)
                    Result.failure()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                stateKeeper.updateDataItemState(dataItem, STATE_NOT_DOWNLOADED)
                Result.failure()
            }
        }
    }
}