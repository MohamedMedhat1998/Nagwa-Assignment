package com.mohamed.medhat.nagwaassignment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.domain.UseCaseHandler
import com.mohamed.medhat.nagwaassignment.domain.use_cases.DownloadMedia
import com.mohamed.medhat.nagwaassignment.domain.use_cases.LoadData
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityState.*
import com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityStateHolder
import com.mohamed.medhat.nagwaassignment.utils.int_defs.DownloadStateHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An mvvm [ViewModel] for the [MainActivity].
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val loadData: LoadData,
    private val downloadMedia: DownloadMedia,
    private val useCaseHandler: UseCaseHandler
) : ViewModel() {

    private val _state = MutableLiveData<ActivityStateHolder>()
    val state: LiveData<ActivityStateHolder>
        get() = _state

    private val _data = MutableLiveData<List<DataItem>>()
    val data: LiveData<List<DataItem>>
        get() = _data

    private val _itemState = MutableLiveData<Pair<DataItem, DownloadStateHolder>>()
    val itemState: LiveData<Pair<DataItem, DownloadStateHolder>>
        get() = _itemState

    init {
        _state.postValue(ActivityStateHolder(STATE_LOADING))
        viewModelScope.launch {
            useCaseHandler.execute(
                loadData,
                UseCase.NoRequestValues(),
                onSuccess = {
                    _data.postValue(it.data)
                    _state.postValue(ActivityStateHolder(STATE_NORMAL))
                },
                onError = {
                    _state.postValue(ActivityStateHolder(STATE_ERROR, it))
                })
        }
    }

    /**
     * Downloads the passed data item's media file.
     * @param dataItem The data to download.
     */
    fun downloadData(dataItem: DataItem) {
        viewModelScope.launch {
            useCaseHandler.execute(
                downloadMedia,
                DownloadMedia.DownloadMediaRequestValues(dataItem),
                onProgress = {
                    _itemState.postValue(it.dataItem to it.progress)
                })
        }
    }
}