package com.mohamed.medhat.nagwaassignment.repository

import com.mohamed.medhat.nagwaassignment.di.FakeApiInstance
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.networking.FakeApi
import com.mohamed.medhat.nagwaassignment.networking.NetworkApi
import javax.inject.Inject

/**
 * A [Repository] that provides a data coming from the [FakeApi].
 */
class FakeRepository @Inject constructor(
    @FakeApiInstance private val api: NetworkApi
) : Repository {

    @Throws(Exception::class)
    override suspend fun getData(): List<DataItem> {
        val response = api.getLearningData()
        if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                return data
            }
            throw Exception("NullDataException")
        }
        throw Exception("FailedResponseException")
    }
}