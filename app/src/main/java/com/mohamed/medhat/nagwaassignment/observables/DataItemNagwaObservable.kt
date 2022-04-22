package com.mohamed.medhat.nagwaassignment.observables

import com.mohamed.medhat.nagwaassignment.model.DataItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataItemNagwaObservable @Inject constructor() : NagwaObservable<DataItem, NagwaObserver<DataItem>> {

    private val observers = mutableListOf<NagwaObserver<DataItem>>()

    override fun registerObserver(o: NagwaObserver<DataItem>) {
        observers.add(o)
    }

    override fun notifyChanges(d: DataItem) {
        observers.forEach {
            it.onUpdate(d)
        }
    }

    override fun removeObserver(o: NagwaObserver<DataItem>) {
        observers.remove(o)
    }
}