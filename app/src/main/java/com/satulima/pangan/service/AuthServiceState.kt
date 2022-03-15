package com.satulima.pangan.service

data class AuthServiceState<out T>(val status: Status, val data: T?, val message: String?){
    companion object{
        fun <T> success(data: T?): AuthServiceState<T>{
            return AuthServiceState(Status.SUCCESS, data, null)
        }
        fun <T> error(msg: String) : AuthServiceState<T>{
            return AuthServiceState(Status.ERROR, null, msg)
        }
        fun <T> loading(): AuthServiceState<T>{
            return AuthServiceState(Status.LOADING, null, null)
        }
    }
}

enum class Status{
    SUCCESS,
    ERROR,
    LOADING,
}
