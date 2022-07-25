package com.matveev.tinkoff.fintex.data.model

import com.google.gson.annotations.SerializedName

data class GifsResult(
    val description: String,
    @SerializedName("gifURL") val url: String
)
