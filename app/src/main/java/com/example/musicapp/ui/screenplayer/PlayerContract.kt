package com.example.musicapp.ui.screenplayer

import com.example.musicapp.data.model.Song

interface PlayerContract {
    interface View {
        fun loadSuccess(songs: List<Song>)
        fun showError(error: String)
    }

    interface Presenter {
        fun getLocalSong()

    }
}
