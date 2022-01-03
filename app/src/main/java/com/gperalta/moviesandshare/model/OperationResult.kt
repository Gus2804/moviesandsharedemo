package com.gperalta.moviesandshare.model

sealed class OperationResult<out T: Any> {
    data class Success<out T : Any>(val data: T? = null) : OperationResult<T>()
    data class Error(val throwable: Throwable) : OperationResult<Nothing>()
}