package com.mohamed.medhat.nagwaassignment.domain

import com.mohamed.medhat.nagwaassignment.di.FakeRepo
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Loads the list of data to be displayed in the UI.
 */
class LoadData @Inject constructor(@FakeRepo val repository: Repository) :
    UseCase<UseCase.NoRequestValues, LoadData.DataResponseValues>() {

    /**
     * Holds a list of [DataItem]s as a response value for this use case.
     */
    class DataResponseValues(val data: List<DataItem>) : ResponseValues

    override suspend fun executeUseCase(requestValues: NoRequestValues?) {
        withContext(Dispatchers.IO) {
            try {
                val data = repository.getData()
                onSuccess.invoke(DataResponseValues(data))
            } catch (e: Exception) {
                e.printStackTrace()
                onError.invoke(e.message ?: "Unknown error occurred!")
            }
        }
    }
}