package com.mohamed.medhat.nagwaassignment.domain.use_cases

import com.mohamed.medhat.nagwaassignment.di.FakeRepo
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.domain.UseCase.NoProgressValues
import com.mohamed.medhat.nagwaassignment.domain.UseCase.NoRequestValues
import com.mohamed.medhat.nagwaassignment.domain.use_cases.LoadData.DataResponseValues
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.repository.Repository
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Loads the list of data to be displayed in the UI.
 */
class LoadData @Inject constructor(@FakeRepo val repository: Repository) :
    UseCase<NoRequestValues, DataResponseValues, NoProgressValues>() {

    /**
     * Holds a list of [DataItem]s as a response value for this use case.
     */
    class DataResponseValues(val data: List<DataItem>) : ResponseValues

    override suspend fun executeUseCase(requestValues: NoRequestValues?) {
        withContext(Dispatchers.IO) {
            try {
                val data = repository.getData()
                // TODO update the dataItem.state
                data.forEach {
                    it.state = DownloadStateHolder(DownloadState.STATE_NOT_DOWNLOADED)
                }
                onSuccess.invoke(DataResponseValues(data))
            } catch (e: Exception) {
                e.printStackTrace()
                onError.invoke(e.message ?: "Unknown error occurred!")
            }
        }
    }
}