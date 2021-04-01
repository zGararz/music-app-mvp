package com.example.musicapp.data.repository

import android.util.Log
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.source.SongDataSource
import com.example.musicapp.utils.OnDataLoadCallBack

class SongRepository private constructor(val localSource: SongDataSource) : SongDataSource {
    override fun getSong(callback: OnDataLoadCallBack<List<Song>>) {
        localSource.getSong(callback)
    }

    companion object {
        private var instance: SongRepository? = null;
        fun getInstance(localSource: SongDataSource) =
            instance ?: SongRepository(localSource).also { instance = it }
    }
}
