package com.matveev.tinkoff.fintex.data.api.categories

import com.matveev.tinkoff.fintex.data.model.GifsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GifsApiService {

    @GET("{categories}/{page}")
    suspend fun getGifs(
        @Path(value = "categories") categories: String = "latest",
        @Path(value = "page") page: Int = 0,
        @Query("json") json: Boolean = true,
    ): GifsResponse
}