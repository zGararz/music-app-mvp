package com.example.musicapp.utils

import android.os.AsyncTask

class LoadDataAsyncTask<T>(
    val callback: OnDataLoadCallBack<T>,
    val handle: () -> T?
) : AsyncTask<Unit, Unit, T>() {
    override fun doInBackground(vararg params: Unit?): T? {
        try {
            return handle()
        } catch (e: Exception) {
            return null
        }
    }

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        result?.let { callback.onSuccess(it) } ?: callback.onFail("Can't load!")
    }
}
