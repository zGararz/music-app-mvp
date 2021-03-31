package com.example.musicapp.data.source.local

import android.annotation.SuppressLint
import android.content.Context
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource
import com.example.musicapp.utils.LoadDataAsyncTask
import com.example.musicapp.utils.OnDataLoadCallBack

@Suppress("DEPRECATION")
class SongLocalDataSource private constructor(val context: Context) : SongDataSource {
    override fun getSong(callback: OnDataLoadCallBack<List<Song>>) {
        LoadDataAsyncTask(callback) {
            SongLocalHandler(context).getLocalSong()
        }.execute()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SongLocalDataSource? = null
        fun getInstance(context: Context) =
            instance ?: SongLocalDataSource(context).also { instance = it }
    }
}
