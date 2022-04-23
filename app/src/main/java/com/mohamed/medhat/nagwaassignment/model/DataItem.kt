package com.mohamed.medhat.nagwaassignment.model

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName
import com.mohamed.medhat.nagwaassignment.R
import com.mohamed.medhat.nagwaassignment.utils.Constants
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder

/**
 * A POJO for the json data item coming from the server response.
 */
data class DataItem(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("url")
    val url: String,

    var state: DownloadStateHolder = DownloadStateHolder(DownloadState.STATE_NOT_DOWNLOADED)
) {
    /**
     * @return The filename with the extension.
     */
    fun getFileName(): String {
        return "$name.${url.takeLast(3)}"
    }

    /**
     * @return The media type that can be passed to an intent so that it can open it using ACTION_VIEW.
     */
    fun getMediaType(): String {
        return if (type == Constants.TYPE_PDF) {
            "application/pdf"
        } else {
            "video/*"
        }
    }

    /**
     * @return The suitable drawable id to be used for action buttons.
     */
    @DrawableRes
    fun getActionButtonIcon(): Int {
        return if (type == Constants.TYPE_PDF) {
            R.drawable.ic_read
        } else {
            R.drawable.ic_play
        }
    }

    /**
     * @return The suitable drawable id to be used for the media indicator.
     */
    @DrawableRes
    fun getMediaLogo(): Int {
        return if (type == Constants.TYPE_PDF) {
            R.drawable.ic_pdf
        } else {
            R.drawable.ic_video
        }
    }
}
