package com.example.musicapp.data.source.local

import android.content.Context
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource
import com.example.musicapp.utils.LoadDataAsyncTask
import com.example.musicapp.utils.OnDataLoadCallBack

class SongLocalDataSource private constructor(val context: Context) : SongDataSource {
    override fun getSong(callback: OnDataLoadCallBack<List<Song>>) {
        LoadDataAsyncTask(callback) {
            SongLocalHandler().getLocalSong(context)
        }.execute()
    }

    companion object {
        private var instance: SongLocalDataSource? = null
        fun getInstance(context: Context) =
            instance ?: SongLocalDataSource(context).also { instance = it }
    }
}
