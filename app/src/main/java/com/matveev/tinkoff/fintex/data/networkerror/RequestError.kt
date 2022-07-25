package com.matveev.tinkoff.fintex.data.networkerror

import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object RequestError {
    fun getErrorMessage(e: Exception): String {
        return when(e){
            is UnknownHostException -> StatusError.NO_CONNECTION
            is HttpException -> StatusError.SERVER_ERROR
            is SocketTimeoutException -> StatusError.TIMEOUT_ERROR
            is NullPointerException -> StatusError.NULL_POINTER
            else -> StatusError.UNKNOWN_ERROR
        }
    }
}
