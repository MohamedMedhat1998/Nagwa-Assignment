package com.mohamed.medhat.nagwaassignment.utils.int_defs;

import static com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.STATE_DOWNLOADED;
import static com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.STATE_DOWNLOADING;
import static com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadState.STATE_NOT_DOWNLOADED;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents the download state of a data item.
 */
@IntDef({STATE_NOT_DOWNLOADED, STATE_DOWNLOADING, STATE_DOWNLOADED})
@Retention(RetentionPolicy.SOURCE)
public @interface DownloadState {
    public static final int STATE_NOT_DOWNLOADED = 0;
    public static final int STATE_DOWNLOADING = 1;
    public static final int STATE_DOWNLOADED = 2;
}
