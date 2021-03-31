package com.example.musicapp.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.musicapp.R
import com.example.musicapp.data.model.Song

const val REQUEST_CODE = 0
const val CHANNEL_ID = "2"
const val CHANNEL_NAME = "Music"
const val NOTIFY_ID = 2
const val ACTION_PLAY = "playaction"
const val ACTION_NEXT = "nextaction"
const val ACTION_PREV = "prevaction"
const val TITLE_NEXT = "next"
const val TITLE_PLAY = "play"
const val TITLE_PREV = "prev"

@SuppressLint("ServiceCast")
class MusicNotification(val context: Context) {
    val notifiManager: NotificationManager

    init {
        notifiManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        createChannel()
    }

    private fun createChannel() {
        val channel = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        notifiManager.createNotificationChannel(channel)
    }

    fun createNotification(song: Song, buttonPlay: Int) {

        val intentPlay = Intent(context, NotificationSender::class.java).setAction(ACTION_PLAY)
        val pendingIntentPlay = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intentPlay,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentNext = Intent(context, NotificationSender::class.java).setAction(ACTION_NEXT)
        val pendingIntentNext = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intentNext,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentPrev = Intent(context, NotificationSender::class.java).setAction(ACTION_PREV)
        val pendingIntentPrev = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intentPrev,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_music_video_24)
            .setContentTitle(song.title)
            .setContentText(song.artist)
            .addAction(R.drawable.ic_baseline_skip_previous_24, TITLE_NEXT, pendingIntentNext)
            .addAction(buttonPlay, TITLE_PLAY, pendingIntentPlay)
            .addAction(R.drawable.ic_baseline_skip_next_24, TITLE_PREV, pendingIntentPrev)
            .setOnlyAlertOnce(true)
            .setShowWhen(false)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
        notifiManager.notify(NOTIFY_ID, notification)
    }

    fun cancelNotification() {
        notifiManager.cancelAll()
    }
}
