package com.mohamed.medhat.nagwaassignment.domain.use_cases

import android.util.Log
import androidx.lifecycle.Observer
import androidx.work.*
import androidx.work.NetworkType.CONNECTED
import com.google.gson.Gson
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.domain.UseCase.NoResponseValues
import com.mohamed.medhat.nagwaassignment.domain.use_cases.DownloadMedia.DownloadMediaRequestValues
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.*
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder
import com.mohamed.medhat.nagwaassignment.workers.DownloadWorker
import com.mohamed.medhat.nagwaassignment.workers.INPUT_DATA_DATA_ITEM
import com.mohamed.medhat.nagwaassignment.workers.PROGRESS_DATA_PROGRESS
import javax.inject.Inject

private const val TAG = "DownloadMedia"

/**
 * Downloads a specific media file.
 */
class DownloadMedia @Inject constructor(private val workManager: WorkManager) :
    UseCase<DownloadMediaRequestValues, NoResponseValues, DownloadMedia.DownloadMediaProgressValues>() {

    /**
     * Holds the [DataItem] whose media will be downloaded using this use case.
     */
    class DownloadMediaRequestValues(val dataItem: DataItem) : RequestValues

    /**
     * Holds the [DownloadStateHolder] that keeps tracking of the download progress.
     */
    class DownloadMediaProgressValues(val dataItem: DataItem, val progress: DownloadStateHolder) :
        ProgressValues

    override suspend fun executeUseCase(requestValues: DownloadMediaRequestValues?) {
        if (requestValues == null) {
            Log.e(TAG, "executeUseCase: requestValues can't be null!")
            return
        }

        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .addTag(requestValues.dataItem.id.toString())
            .setConstraints(Constraints.Builder().setRequiredNetworkType(CONNECTED).build())
            .setInputData(
                workDataOf(Pair(INPUT_DATA_DATA_ITEM, Gson().toJson(requestValues.dataItem)))
            ).build()

        workManager.beginUniqueWork(
            requestValues.dataItem.id.toString(),
            ExistingWorkPolicy.KEEP,
            downloadWorkRequest
        ).enqueue()

        val updates = workManager.getWorkInfoByIdLiveData(downloadWorkRequest.id)
        val updatesObserver: Observer<WorkInfo> = object : Observer<WorkInfo> {
            override fun onChanged(t: WorkInfo?) {
                if (t != null) {
                    val progress = t.progress.getInt(PROGRESS_DATA_PROGRESS, 0)
                    Log.d(TAG, "onChanged: Progress: $progress")
                    onProgress.invoke(
                        DownloadMediaProgressValues(
                            requestValues.dataItem,
                            DownloadStateHolder(
                                STATE_DOWNLOADING,
                                progress
                            )
                        )
                    )
                    val state = t.state
                    if (state == WorkInfo.State.SUCCEEDED) {
                        onProgress.invoke(
                            DownloadMediaProgressValues(
                                requestValues.dataItem,
                                DownloadStateHolder(
                                    STATE_DOWNLOADED
                                )
                            )
                        )
                        Log.d(TAG, "onChanged: Observer removed!")
                        updates.removeObserver(this)
                    } else if (state == WorkInfo.State.FAILED) {
                        onProgress.invoke(
                            DownloadMediaProgressValues(
                                requestValues.dataItem,
                                DownloadStateHolder(
                                    STATE_NOT_DOWNLOADED
                                )
                            )
                        )
                        Log.d(TAG, "onChanged: Observer removed!")
                        updates.removeObserver(this)
                    }
                }
            }
        }
        updates.observeForever(updatesObserver)
    }
}
