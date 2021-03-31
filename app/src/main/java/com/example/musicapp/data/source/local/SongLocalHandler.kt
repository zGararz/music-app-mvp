package com.example.musicapp.data.source.local

import android.content.Context
import android.provider.MediaStore
import com.example.musicapp.data.model.Song

class SongLocalHandler {
    fun getLocalSong(context: Context): List<Song>? {
        val list = ArrayList<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                do {
                    list.add(ParseCursor().SongParseCursor(it))
                } while (it.moveToNext())
            }
            return list
        }
        return null
    }
}
