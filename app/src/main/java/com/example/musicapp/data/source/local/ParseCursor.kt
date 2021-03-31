package com.example.musicapp.data.source.local

import android.database.Cursor
import android.provider.MediaStore
import com.example.musicapp.data.model.Song

class ParseCursor {
    fun SongParseCursor(cursor: Cursor): Song {
        return Song(
            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),

            )
    }
}
