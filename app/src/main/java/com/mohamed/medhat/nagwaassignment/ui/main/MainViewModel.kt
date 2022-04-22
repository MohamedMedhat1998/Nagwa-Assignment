package com.mohamed.medhat.nagwaassignment.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.nagwaassignment.domain.UseCase
import com.mohamed.medhat.nagwaassignment.domain.UseCaseHandler
import com.mohamed.medhat.nagwaassignment.domain.use_cases.CancelDownload
import com.mohamed.medhat.nagwaassignment.domain.use_cases.DeleteMedia
import com.mohamed.medhat.nagwaassignment.domain.use_cases.DownloadMedia
import com.mohamed.medhat.nagwaassignment.domain.use_cases.LoadData
import com.mohamed.medhat.nagwaassignment.model.DataItem
import com.mohamed.medhat.nagwaassignment.observables.DataItemNagwaObservable
import com.mohamed.medhat.nagwaassignment.observables.NagwaObserver
import com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityState.*
import com.mohamed.medhat.nagwaassignment.utils.int_defs.ActivityStateHolder
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
    private val cancelDownload: CancelDownload,
    private val deleteMedia: DeleteMedia,
    private val useCaseHandler: UseCaseHandler,
    private val observable: DataItemNagwaObservable
) : ViewModel(), NagwaObserver<DataItem> {

    private val _state = MutableLiveData<ActivityStateHolder>()
    val state: LiveData<ActivityStateHolder>
        get() = _state

    private val _data = MutableLiveData<List<DataItem>>()
    val data: LiveData<List<DataItem>>
        get() = _data

    private val _newItemState = MutableLiveData<DataItem>()
    val newItemState: LiveData<DataItem>
        get() = _newItemState

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    init {
        observable.registerObserver(this)
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
                DownloadMedia.DownloadMediaRequestValues(dataItem)
            )
        }
    }

    /**
     * Cancels the ongoing download of the passed [dataItem].
     * @param dataItem The [DataItem] whose download will be cancelled.
     */
    fun cancelDownload(dataItem: DataItem) {
        viewModelScope.launch {
            useCaseHandler.execute(
                cancelDownload,
                CancelDownload.CancelDownloadRequestValues(dataItem)
            )
        }
    }

    /**
     * Deletes the media of the passed [dataItem].
     * @param dataItem The [DataItem] whose media should be deleted.
     */
    fun deleteMedia(dataItem: DataItem) {
        viewModelScope.launch {
            useCaseHandler.execute(
                deleteMedia,
                DeleteMedia.DeleteMediaRequestValues(dataItem),
                onError = {
                    _toastMessage.postValue(it)
                }
            )
        }
    }

    override fun onUpdate(d: DataItem) {
        _newItemState.postValue(d)
    }

    override fun onCleared() {
        super.onCleared()
        observable.removeObserver(this)
    }
}