package com.matveev.tinkoff.fintex.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matveev.tinkoff.fintex.data.model.GifsResponse
import com.matveev.tinkoff.fintex.data.repository.GifsRepository
import com.matveev.tinkoff.fintex.ui.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class GifViewModel(
    private val GIfRepository: GifsRepository,
    private val categoriesName: String,
    private val page: Int,
) : ViewModel() {

    private val _categoriesGifLiveData = MutableLiveData<ViewState<GifsResponse>>()
    val categoriesGifLiveData: LiveData<ViewState<GifsResponse>>
        get() = _categoriesGifLiveData

    init {
        load(categoriesName, page)
    }

    fun load(categoriesName: String, page: Int){
        if (_categoriesGifLiveData.value is ViewState.Loading) return
        viewModelScope.launch(Dispatchers.IO) {
            _categoriesGifLiveData.postValue(ViewState.Loading())
            try {
                _categoriesGifLiveData.postValue(ViewState.Success(data = GIfRepository.getGifs(categoriesName, page)))
            } catch (e: Exception){
                _categoriesGifLiveData.postValue(ViewState.Error(exception = e))
            }
        }
    }
}