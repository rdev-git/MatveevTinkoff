package com.matveev.tinkoff.fintex.data.repository

import com.matveev.tinkoff.fintex.data.api.categories.GifsApiService

class GifsRepository(private val gifsApiService: GifsApiService) {
    suspend fun getGifs(categoriesName: String, page: Int) =
        gifsApiService.getGifs(categoriesName, page)
}