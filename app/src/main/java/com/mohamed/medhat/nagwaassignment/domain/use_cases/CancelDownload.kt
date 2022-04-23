package com.mohamed.medhat.nagwaassignment.domain.use_cases

import android.util.Log
import androidx.work.WorkManager
import com.mohamed.medhat.nagwaassignment.di.PrefsStateKeeper
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.observables.DataItemNagwaObservable
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.STATE_NOT_DOWNLOADED
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder
import com.mohamed.medhat.nagwaassignment.utils.state.DataItemStateKeeper
import javax.inject.Inject

private const val TAG = "CancelDownload"

/**
 * Cancels the ongoing download of a specific [DataItem].
 */
class CancelDownload @Inject constructor(
    private val workManager: WorkManager,
    @PrefsStateKeeper private val stateKeeper: DataItemStateKeeper,
    private val observable: DataItemNagwaObservable
) :
    UseCase<CancelDownload.CancelDownloadRequestValues, UseCase.NoResponseValues>() {

    /**
     * Holds the [DataItem] whose download will be canceled.
     */
    class CancelDownloadRequestValues(val dataItem: DataItem) : RequestValues

    override suspend fun executeUseCase(requestValues: CancelDownloadRequestValues?) {
        if (requestValues == null) {
            Log.e(TAG, "executeUseCase: request values can't be null!")
            return
        }
        workManager.cancelUniqueWork(requestValues.dataItem.id.toString())
        stateKeeper.updateDataItemState(requestValues.dataItem, STATE_NOT_DOWNLOADED)
        observable.notifyChanges(
            requestValues.dataItem.copy(
                state = DownloadStateHolder(
                    STATE_NOT_DOWNLOADED
                )
            )
        )
        onSuccess.invoke(NoResponseValues())
    }
}