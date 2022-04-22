package com.mohamed.medhat.nagwaassignment.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.download.DownloadManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val INPUT_DATA_DATA_ITEM = "input-data-item"
private const val TAG = "DownloadWorker"

/**
 * Responsible for the download operation.
 */
@HiltWorker
class DownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val downloadManager: DownloadManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val dataItemJson = inputData.getString(INPUT_DATA_DATA_ITEM) ?: return Result.failure()
        val dataItem =
            Gson().fromJson(dataItemJson, DataItem::class.java) ?: return Result.failure()
        val extension = dataItem.url.takeLast(3)
        return withContext(Dispatchers.IO) {
            try {
                val isSuccessful =
                    downloadManager.download(dataItem.url, "${dataItem.id}.$extension") {
                        Log.d(TAG, "doWork: progress: ${it}%")
                        // TODO publish this progress outside the work manager.
                        // TODO create a notification with the progress.
                    }
                if (isSuccessful) {
                    Result.success()
                } else {
                    Result.failure()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure()
            }
        }
    }
}