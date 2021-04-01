package com.example.musicapp.service

import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import com.example.musicapp.data.model.Song

class MusicService : Service(), MusicControl, MusicTracker {
    var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder {
        return SongBinder(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        stopSelf()
    }

    override fun create(song: Song) {

        mediaPlayer?.release()
        val uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            song.id.toLong()
        )
        mediaPlayer = MediaPlayer.create(applicationContext, uri)
    }

    override fun play() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun seekTo(newPosition: Int) {
        mediaPlayer?.seekTo(newPosition)
    }

    override fun getDuration(): Int = mediaPlayer?.duration ?: 0

    override fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0

    override fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    class SongBinder(private val service: MusicService) : Binder() {
        fun getService(): MusicService = service
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MusicService::class.java)
    }
}
