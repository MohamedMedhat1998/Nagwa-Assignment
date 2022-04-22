package com.mohamed.medhat.nagwaassignment.utils.state

import android.content.SharedPreferences
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState
import javax.inject.Inject
import javax.inject.Singleton

const val PREFS_DATA_ITEM_STATE = "data-item-state"

/**
 * A [DataItemStateKeeper] packed by the [SharedPreferences].
 */
@Singleton
class PrefsDataItemStateKeeper @Inject constructor(private val prefs: SharedPreferences) :
    DataItemStateKeeper {
    override fun updateDataItemState(dataItem: DataItem, @DownloadState state: Int) {
        prefs.edit().putInt("$PREFS_DATA_ITEM_STATE.${dataItem.id}", state).apply()
    }

    @DownloadState
    override fun getDataItemState(dataItem: DataItem): Int {
        return prefs.getInt(
            "$PREFS_DATA_ITEM_STATE.${dataItem.id}",
            DownloadState.STATE_NOT_DOWNLOADED
        )
    }
}