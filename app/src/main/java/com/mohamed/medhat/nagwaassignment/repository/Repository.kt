package com.mohamed.medhat.nagwaassignment.repository

import com.mohamed.medhat.nagwaassignment.model.DataItem

/**
 * The single source of truth for the app to get the data from.
 */
interface Repository {
    /**
     * Gets the data to be used inside the app.
     * @return A list of [DataItem]s.
     */
    suspend fun getData(): List<DataItem>
}