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

private const val REQUEST_CODE = 0
private const val CHANNEL_ID = "2"
private const val CHANNEL_NAME = "Music"
private const val NOTIFY_ID = 2
private const val TITLE_NEXT = "next"
private const val TITLE_PLAY = "play"
private const val TITLE_PREV = "prev"
const val ACTION_PLAY = "playAction"
const val ACTION_NEXT = "nextAction"
const val ACTION_PREV = "prevAction"

@SuppressLint("ServiceCast")
class MusicNotification(val context: Context) {
    private val notificationManager: NotificationManager =
        context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannel()
    }

    private fun createChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
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
        notificationManager.notify(NOTIFY_ID, notification)
    }

    fun cancelNotification() {
        notificationManager.cancelAll()
    }
}
