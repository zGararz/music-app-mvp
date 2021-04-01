package com.example.musicapp.data.source.local

import android.annotation.SuppressLint
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource
import com.example.musicapp.utils.LoadDataAsyncTask
import com.example.musicapp.utils.OnDataLoadCallBack

@Suppress("DEPRECATION")
class SongLocalDataSource private constructor(
    private val handler: SongLocalHandler
) : SongDataSource {

    override fun getSong(callback: OnDataLoadCallBack<List<Song>>) {
        LoadDataAsyncTask(callback) {
            handler.getLocalSong()
        }.execute()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SongLocalDataSource? = null
        fun getInstance(handler: SongLocalHandler) =
            instance ?: SongLocalDataSource(handler).also { instance = it }
    }
}
