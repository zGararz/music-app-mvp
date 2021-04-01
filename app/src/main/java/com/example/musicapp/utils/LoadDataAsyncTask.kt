@file:Suppress("DEPRECATION")

package com.example.musicapp.utils

import android.os.AsyncTask

class LoadDataAsyncTask<T>(
    private val callback: OnDataLoadCallBack<T>,
    private val handle: () -> T?
) : AsyncTask<Unit, Unit, T>() {
    override fun doInBackground(vararg params: Unit?): T? {
        return try {
            handle()
        } catch (e: Exception) {
            null
        }
    }

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        result?.let { callback.onSuccess(it) } ?: callback.onFail("Can't load!")
    }
}
