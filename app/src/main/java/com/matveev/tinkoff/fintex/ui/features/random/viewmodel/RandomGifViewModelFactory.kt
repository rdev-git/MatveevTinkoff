package com.matveev.tinkoff.fintex.ui.features.random.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matveev.tinkoff.fintex.data.repository.RandomGifRepository

class RandomGifViewModelFactory(
    private val randomGifRepository: RandomGifRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        RandomGifViewModel(randomGifRepository) as T

}