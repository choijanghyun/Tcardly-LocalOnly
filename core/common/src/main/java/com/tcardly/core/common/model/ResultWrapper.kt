package com.tcardly.core.common.model

sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val message: String, val throwable: Throwable? = null) : ResultWrapper<Nothing>()
    data object Loading : ResultWrapper<Nothing>()
}
