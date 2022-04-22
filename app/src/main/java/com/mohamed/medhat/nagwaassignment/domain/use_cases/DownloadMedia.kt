package com.mohamed.medhat.nagwaassignment.domain.use_cases

import android.util.Log
import androidx.work.*
import androidx.work.NetworkType.CONNECTED
import com.google.gson.Gson
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.domain.UseCase.NoResponseValues
import com.mohamed.medhat.nagwaassignment.domain.use_cases.DownloadMedia.DownloadMediaRequestValues
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.workers.DownloadWorker
import com.mohamed.medhat.nagwaassignment.workers.INPUT_DATA_DATA_ITEM
import javax.inject.Inject

private const val TAG = "DownloadMedia"

/**
 * Downloads a specific media file.
 */
class DownloadMedia @Inject constructor(private val workManager: WorkManager) :
    UseCase<DownloadMediaRequestValues, NoResponseValues>() {

    /**
     * Holds the [DataItem] whose media will be downloaded using this use case.
     */
    class DownloadMediaRequestValues(val dataItem: DataItem) : RequestValues

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
    }
}
