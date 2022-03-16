package com.satulima.pangan.service

data class StatusState<out T>(val status: Status, val data: T?, val message: String?){
    companion object{
        fun <T> success(data: T?): StatusState<T>{
            return StatusState(Status.SUCCESS, data, null)
        }
        fun <T> error(msg: String) : StatusState<T>{
            return StatusState(Status.ERROR, null, msg)
        }
        fun <T> loading(): StatusState<T>{
            return StatusState(Status.LOADING, null, null)
        }
    }
}

enum class Status{
    SUCCESS,
    ERROR,
    LOADING,
}
