package com.mohamed.medhat.nagwaassignment.observables

/**
 * An observable type that can be observed by [NagwaObserver]s.
 */
interface NagwaObservable<D, O : NagwaObserver<D>> {
    /**
     * Adds an observer of type [O] to the observers list.
     * @param o The observer to register.
     */
    fun registerObserver(o: O)

    /**
     * Notifies all the registered observers with the new data [d].
     * @param d The new updated data.
     */
    fun notifyChanges(d: D)

    /**
     * Removes an observer from the observers list.
     * @param o The observer to remove.
     */
    fun removeObserver(o: O)
}