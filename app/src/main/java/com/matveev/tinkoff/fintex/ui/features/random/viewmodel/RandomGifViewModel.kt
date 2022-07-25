package com.matveev.tinkoff.fintex.ui.features.random.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matveev.tinkoff.fintex.data.model.RandomGifResponse
import com.matveev.tinkoff.fintex.data.repository.RandomGifRepository
import com.matveev.tinkoff.fintex.ui.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.collections.ArrayList

class RandomGifViewModel(private val randomGifRepository: RandomGifRepository) : ViewModel() {

    private val _gifLiveData = MutableLiveData<ViewState<RandomGifResponse>>()
    val gifLiveData : LiveData<ViewState<RandomGifResponse>>
        get() = _gifLiveData

    private val _backAllowedLiveData = MutableLiveData<Boolean>()
    val backAllowedLiveData : LiveData<Boolean>
        get() = _backAllowedLiveData

    private val gifs = ArrayList<Gif>()
    private var currentUrlPosition = 0

    init {
        load()
    }

    fun nextGif() {
        if (currentUrlPosition + 1 < gifs.size) {
            _gifLiveData.value = ViewState.Success(
                RandomGifResponse(
                    description = gifs[currentUrlPosition + 1].description,
                    url = gifs[currentUrlPosition + 1].url
                )
            )
            currentUrlPosition++
            _backAllowedLiveData.value = currentUrlPosition!=0
        } else load()

    }

    fun previousGif() {
        if (currentUrlPosition != 0) {
            _gifLiveData.value = ViewState.Success(
                RandomGifResponse(
                    description = gifs[currentUrlPosition - 1].description,
                    url = gifs[currentUrlPosition - 1].url
                )
            )
            currentUrlPosition--
            _backAllowedLiveData.value = currentUrlPosition!=0
        }
    }

    fun load() {
        if (_gifLiveData.value is ViewState.Loading) return
        viewModelScope.launch(Dispatchers.IO) {
            _gifLiveData.postValue(ViewState.Loading())
            try {
                val data = randomGifRepository.getRandomGif()
                _gifLiveData.postValue(ViewState.Success(data = data))

                gifs.add(
                    Gif(
                        url = data.url,
                        description = data.description
                    )
                )
                currentUrlPosition = gifs.size - 1
                _backAllowedLiveData.postValue(currentUrlPosition!=0)

            } catch (e: Exception) {
                _gifLiveData.postValue(ViewState.Error(exception = e))
                _backAllowedLiveData.postValue(currentUrlPosition!=0)
            }
        }
    }

}