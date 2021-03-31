package com.example.musicapp.ui.screenplayer

import com.example.musicapp.data.model.Song

interface PlayerContract {
    interface View {
        fun loadSuccess(list: List<Song>)
        fun showError(e: String)
        fun setSongInfo(song: Song)
        fun setPlayButton(b: Boolean)
        fun setSeekBar(process: Int)
        fun setMaxSeekBar(max: Int)
        fun setTime(time: Int)
    }

    interface Presenter {
        fun getLocalSong()
        fun handleCreateSong()
        fun handlePlaySong(b: Boolean?)
        fun handleNextSong()
        fun handlePrevSong()
        fun handlerSeek(p: Int?)

    }
}
