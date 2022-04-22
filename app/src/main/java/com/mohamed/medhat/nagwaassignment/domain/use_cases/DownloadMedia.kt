package com.mohamed.medhat.nagwaassignment.domain.use_cases

import android.content.Context
import android.util.Log
import androidx.work.*
import com.google.gson.Gson
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.domain.UseCase.NoResponseValues
import com.mohamed.medhat.nagwaassignment.domain.use_cases.DownloadMedia.DownloadMediaRequestValues
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.workers.DownloadWorker
import com.mohamed.medhat.nagwaassignment.workers.INPUT_DATA_DATA_ITEM
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private const val TAG = "DownloadMedia"

/**
 * Downloads a specific media file.
 */
class DownloadMedia @Inject constructor(@ApplicationContext private val context: Context) :
    UseCase<DownloadMediaRequestValues, NoResponseValues>() {

    /**
     * Holds the [DataItem] whose media will be downloaded using this use case.
     */
    class DownloadMediaRequestValues(val dataItem: DataItem) : RequestValues

    override suspend fun executeUseCase(requestValues: DownloadMediaRequestValues?) {
        // TODO download the file
        // TODO publish progress
        // TODO update the state
        if (requestValues == null) {
            Log.e(TAG, "executeUseCase: requestValues can't be null!")
            return
        }

        // TODO optimize
        val downloadWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .addTag(requestValues.dataItem.id.toString())
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .setInputData(
                workDataOf(Pair(INPUT_DATA_DATA_ITEM, Gson().toJson(requestValues.dataItem)))
            ).build()

        // TODO inject the work manager
        // TODO get the progress from the work manager
        val workManager = WorkManager.getInstance(context)

        workManager.beginUniqueWork(
            requestValues.dataItem.id.toString(),
            ExistingWorkPolicy.KEEP,
            downloadWorkRequest
        ).enqueue()

        // TODO download multiple files
        // TODO on success
        // TODO on fail
    }
}