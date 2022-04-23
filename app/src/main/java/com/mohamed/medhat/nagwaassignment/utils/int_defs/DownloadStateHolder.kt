package com.mohamed.medhat.nagwaassignment.utils.int_defs

import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.STATE_DOWNLOADING

/**
 * Holds the download state of a data item in addition to the progress in case of the [STATE_DOWNLOADING].
 * @param state The [DownloadState] that describes the current state of the item.
 * @param progress Used in cases of [STATE_DOWNLOADING]. Pass `-1` for the indeterminant downloads.
 */
class DownloadStateHolder(@DownloadState val state: Int, val progress: Int = 0)