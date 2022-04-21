package com.mohamed.medhat.nagwaassignment.utils.int_defs

import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.STATE_DOWNLOADING

/**
 * Holds the download state of a data item in addition to the progress in case of the [STATE_DOWNLOADING].
 */
class DownloadStateHolder(@DownloadState val state: Int, val progress: Int = 0)