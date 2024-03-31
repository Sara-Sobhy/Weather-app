package com.example.weather.alert

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
class AlarmStopReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("AlarmStopReceiver", "Broadcast received")
        if (context != null && intent != null) {
            val action = intent.action
            if (action == AlertReceiver.ACTION_STOP_ALARM) {
                Log.i("AlarmStopReceiver", "Action: $action")

                AlertReceiver.mediaPlayer?.stop()

                // Dismiss the notification
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(123)
            }
        }
    }
}