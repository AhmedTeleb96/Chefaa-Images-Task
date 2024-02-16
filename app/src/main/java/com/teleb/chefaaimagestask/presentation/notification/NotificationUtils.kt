package com.teleb.chefaaimagestask.presentation.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.teleb.chefaaimagestask.R
import com.teleb.chefaaimagestask.presentation.main.MainActivity
import javax.inject.Inject

@SuppressLint("ObsoleteSdkInt")
class NotificationUtils @Inject constructor(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun showNotification(title: String, content: String): Notification {
        // Create a notification channel (for Android Oreo and above)
        createNotificationChannel()
        // Create a PendingIntent for when the notification is clicked
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_MUTABLE
        )

        // Build the notification
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(contentIntent)
            .setAutoCancel(false)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "chefaaTask"
            enableLights(true)
            vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            lightColor = context.getColor(R.color.black)
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_NAME = "chefea_channel_name"
        const val NOTIFICATION_CHANNEL_ID = "chefea_channel_id"
    }
}