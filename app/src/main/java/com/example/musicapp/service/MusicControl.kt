package com.example.musicapp.service

import com.example.musicapp.data.model.Song

interface MusicControl {
    fun create(song: Song)
    fun play()
    fun pause()
    fun seekto(p: Int)
}
