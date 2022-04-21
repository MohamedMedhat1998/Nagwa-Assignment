package com.mohamed.medhat.nagwaassignment.utils.int_defs

/**
 * Represents the state object an activity can have in addition to the error message
 * if needed.
 */
class ActivityStateHolder(@ActivityState val state: Int, val error: String = "")