package com.example.musicapp.data.source

import com.example.musicapp.data.model.Song
import com.example.musicapp.utils.OnDataLoadCallBack

interface SongDataSource {
    fun getSong(callback: OnDataLoadCallBack<List<Song>>)
}
