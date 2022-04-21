package com.mohamed.medhat.nagwaassignment.networking

import com.mohamed.medhat.nagwaassignment.model.DataItem
import retrofit2.Response

/**
 * Responsible for fetching whatever data the app needs from the remote server.
 */
interface NetworkApi {
    /**
     * @return A response containing a list of [DataItem]s.
     */
    suspend fun getLearningData(): Response<List<DataItem>>
}