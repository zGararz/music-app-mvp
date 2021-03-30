package com.example.musicapp.data.model

import android.database.Cursor
import android.provider.MediaStore

class Song(val id: Int, val title: String, val artist: String) {
    constructor(cursor: Cursor): this (
            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
    )
}
