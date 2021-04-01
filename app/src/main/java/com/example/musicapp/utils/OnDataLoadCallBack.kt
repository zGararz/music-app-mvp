package com.example.musicapp.utils

interface OnDataLoadCallBack<T> {
    fun onSuccess(data: T?)
    fun onFail(e: String)
}
