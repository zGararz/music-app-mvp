package com.example.musicapp.service

interface MusicTracker {
    fun getDuration(): Int
    fun getCurrentPosition(): Int
    fun isPlaying(): Boolean
}
