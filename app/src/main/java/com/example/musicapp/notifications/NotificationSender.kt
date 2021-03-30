package com.example.musicapp.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

const val ACTION_NAME = "action name"
const val TRACK = "track"
class NotificationSender : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.sendBroadcast(Intent(TRACK).putExtra(ACTION_NAME, intent?.action))
    }
}
