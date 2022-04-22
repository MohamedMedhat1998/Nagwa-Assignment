package com.mohamed.medhat.nagwaassignment.domain.use_cases

import android.content.Context
import android.util.Log
import com.mohamed.medhat.nagwaassignment.R
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.observables.DataItemNagwaObservable
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.STATE_NOT_DOWNLOADED
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

private const val TAG = "DeleteMedia"

/**
 * Deletes a downloaded media from the storage.
 */
class DeleteMedia @Inject constructor(
    @ApplicationContext private val context: Context,
    val observable: DataItemNagwaObservable
) : UseCase<DeleteMedia.DeleteMediaRequestValues, UseCase.NoResponseValues>() {
    /**
     * Holds the [DataItem] whose media will be deleted using this use case.
     */
    class DeleteMediaRequestValues(val dataItem: DataItem) : RequestValues

    override suspend fun executeUseCase(requestValues: DeleteMediaRequestValues?) {
        if (requestValues == null) {
            Log.e(TAG, "executeUseCase: request values can't be null!")
            return
        }
        val dataItem = requestValues.dataItem
        val file = File("${context.filesDir}/${dataItem.id}.${dataItem.url.takeLast(3)}")
        if (file.exists()) {
            if (file.delete()) {
                observable.notifyChanges(
                    dataItem.copy(
                        state = DownloadStateHolder(
                            STATE_NOT_DOWNLOADED
                        )
                    )
                )
            } else {
                onError.invoke(context.getString(R.string.delete_error_message))
            }
        }
    }

}