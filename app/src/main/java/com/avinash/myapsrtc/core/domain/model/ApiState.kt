package com.avinash.myapsrtc.core.domain.model

sealed class ApiState<out T> {
    object Loading: ApiState<Nothing>()
    object Pending: ApiState<Nothing>()
    data class Failure(val message: String): ApiState<Nothing>()
    data class Success<out T>(val data: T): ApiState<T>()

}