package com.matveev.tinkoff.fintex.ui

import java.lang.Exception

sealed class ViewState<out T>{
    data class Success<T>(val data: T?): ViewState<T>()
    data class Error<T>(val exception: Exception): ViewState<T>()
    class Loading<T> : ViewState<T>()
}

