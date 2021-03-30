package com.example.musicapp.data.source.local

import android.content.Context
import android.provider.MediaStore
import com.example.musicapp.data.model.Song

class SongLocalHandler(val context: Context) {
    fun getLocalSong(): List<Song> {
        val songs = mutableListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    songs.add(Song(it))
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        return songs
    }
}
