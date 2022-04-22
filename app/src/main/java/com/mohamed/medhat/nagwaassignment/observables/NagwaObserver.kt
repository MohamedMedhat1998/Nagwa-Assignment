package com.mohamed.medhat.nagwaassignment.observables

/**
 * A type that can receive live updates from the [NagwaObservable].
 */
interface NagwaObserver<D> {
    /**
     * Triggered by the observable whenever a new data update occurs.
     * @param d The new updated data of type [D].
     */
    fun onUpdate(d: D)
}