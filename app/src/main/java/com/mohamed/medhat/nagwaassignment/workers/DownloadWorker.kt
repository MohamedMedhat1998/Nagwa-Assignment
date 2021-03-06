package com.mohamed.medhat.nagwaassignment.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.mohamed.medhat.nagwaassignment.R
import com.mohamed.medhat.nagwaassignment.di.PrefsStateKeeper
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.observables.DataItemNagwaObservable
import com.mohamed.medhat.nagwaassignment.utils.download.DownloadManager
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.*
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder
import com.mohamed.medhat.nagwaassignment.utils.state.DataItemStateKeeper
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
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val downloadManager: DownloadManager,
    @PrefsStateKeeper private val stateKeeper: DataItemStateKeeper,
    private val observable: DataItemNagwaObservable,
    private val notificationManager: NotificationManager
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val dataItemJson = inputData.getString(INPUT_DATA_DATA_ITEM) ?: return Result.failure()
        val dataItem =
            Gson().fromJson(dataItemJson, DataItem::class.java) ?: return Result.failure()
        return withContext(Dispatchers.IO) {
            try {
                val isSuccessful =
                    downloadManager.download(dataItem.url, dataItem.getFileName()) {
                        Log.d(TAG, "doWork: progress: ${it}%")
                        if (!isStopped) {
                            observable.notifyChanges(
                                dataItem.copy(
                                    state = DownloadStateHolder(
                                        STATE_DOWNLOADING, it
                                    )
                                )
                            )
                            if (it % 10 == 0 || it == -1) {
                                val notification =
                                    NotificationCompat.Builder(appContext, "DOWNLOADS")
                                        .setSmallIcon(R.drawable.ic_notification_downloading)
                                        .setContentTitle(dataItem.name)
                                        .setContentText(appContext.getString(R.string.downloading))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                        .setAutoCancel(true)
                                        .setProgress(100, it, it == -1)
                                        .build()
                                notificationManager.notify(dataItem.id, notification)
                            }

                        } else {
                            downloadManager.isCancelled = true
                            observable.notifyChanges(
                                dataItem.copy(
                                    state = DownloadStateHolder(
                                        STATE_NOT_DOWNLOADED, it
                                    )
                                )
                            )
                            notificationManager.cancel(dataItem.id)
                        }
                    }
                if (isSuccessful) {
                    stateKeeper.updateDataItemState(dataItem, STATE_DOWNLOADED)
                    observable.notifyChanges(
                        dataItem.copy(
                            state = DownloadStateHolder(
                                STATE_DOWNLOADED
                            )
                        )
                    )
                    val notification = NotificationCompat.Builder(appContext, "DOWNLOADS")
                        .setSmallIcon(R.drawable.ic_notification_download_completed)
                        .setContentTitle(dataItem.name)
                        .setContentText(appContext.getString(R.string.download_completed))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setProgress(0, 0, false)
                        .build()

                    notificationManager.notify(dataItem.id, notification)
                    Result.success()
                } else {
                    stateKeeper.updateDataItemState(dataItem, STATE_NOT_DOWNLOADED)
                    observable.notifyChanges(
                        dataItem.copy(
                            state = DownloadStateHolder(
                                STATE_NOT_DOWNLOADED
                            )
                        )
                    )
                    notificationManager.cancel(dataItem.id)
                    Result.failure()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                stateKeeper.updateDataItemState(dataItem, STATE_NOT_DOWNLOADED)
                observable.notifyChanges(
                    dataItem.copy(
                        state = DownloadStateHolder(
                            STATE_NOT_DOWNLOADED
                        )
                    )
                )
                notificationManager.cancel(dataItem.id)
                Result.failure()
            }
        }
    }
}