package com.matveev.tinkoff.fintex.ui.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.matveev.tinkoff.fintex.data.repository.GifsRepository

class GifViewModelFactory(
    private val GIfRepository: GifsRepository,
    private val categoriesName: String,
    private val page: Int,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        GifViewModel(GIfRepository, categoriesName, page) as T
}