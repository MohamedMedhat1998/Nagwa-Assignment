package com.mohamed.medhat.nagwaassignment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [MainActivity].
 */
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    // TODO find a way to update the state
    private val _state = MutableLiveData<ActivityStateHolder>()
    val state: LiveData<ActivityStateHolder>
        get() = _state

    // TODO find a way to update the data items
    private val _data = MutableLiveData<List<DataItem>>()
    val data: LiveData<List<DataItem>>
        get() = _data
}