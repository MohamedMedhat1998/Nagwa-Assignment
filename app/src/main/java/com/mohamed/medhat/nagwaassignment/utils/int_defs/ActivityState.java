package com.mohamed.medhat.nagwaassignment.utils.int_defs;

import static com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityState.STATE_ERROR;
import static com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityState.STATE_LOADING;
import static com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityState.STATE_NORMAL;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Represents the possible states the activity can have.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({STATE_NORMAL, STATE_LOADING, STATE_ERROR})
public @interface ActivityState {
    public static int STATE_NORMAL = 0;
    public static int STATE_LOADING = 1;
    public static int STATE_ERROR = 2;
}
