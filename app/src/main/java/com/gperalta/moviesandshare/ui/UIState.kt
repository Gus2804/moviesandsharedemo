package com.gperalta.moviesandshare.ui

sealed class UIState<out T : Any> {
    data class Success<out T: Any>(val data : T? = null) : UIState<T>()
    data class Error(val throwable: Throwable) : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    object Idle : UIState<Nothing>()
}
