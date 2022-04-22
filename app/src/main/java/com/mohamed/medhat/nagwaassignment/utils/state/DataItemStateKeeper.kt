package com.mohamed.medhat.nagwaassignment.utils.state

import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState

/**
 * Keeps the state of the [DataItem].
 */
interface DataItemStateKeeper {
    /**
     * Updates the current state of the passed [dataItem].
     * @param dataItem The [DataItem] to update its state.
     * @param state The new state to set.
     */
    fun updateDataItemState(dataItem: DataItem, @DownloadState state: Int)

    /**
     * @return The previously saved state of the passeed [dataItem].
     */
    @DownloadState
    fun getDataItemState(dataItem: DataItem): Int
}