package com.matveev.tinkoff.fintex.data.repository

import com.matveev.tinkoff.fintex.data.api.random.RandomGifApiService

class RandomGifRepository(private val randomGifApiService: RandomGifApiService) {

    suspend fun getRandomGif() = randomGifApiService.getGif()

}