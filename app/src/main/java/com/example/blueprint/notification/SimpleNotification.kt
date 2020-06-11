package com.example.blueprint.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.blueprint.MainActivity
import com.example.blueprint.R

class SimpleNotification private constructor(private val context: Context) {

    fun createNotification(text: String) {
        createNotificationChannel()
        createSimpleNotification(text)
    }

    private fun createSimpleNotification(text: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Notification.Builder(context)
        } else {
            Notification.Builder(context, CHANNEL_ID_GENERAL)
        }

        val notification = with(builder) {
            setSmallIcon(R.drawable.ic_book)
            setAutoCancel(true)
            setContentTitle(context.getString(R.string.app_name))
            style = Notification.BigTextStyle().bigText(text)
            setContentIntent(pendingIntent)
        }.build()
        context.getSystemService(NotificationManager::class.java)
            ?.run { notify(NOTIFICATION_ID_GENERAL, notification) }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channelTitle = context.getString(R.string.notification_channel)
        val channel = NotificationChannel(CHANNEL_ID_GENERAL, channelTitle, IMPORTANCE_DEFAULT)
            .apply { description = context.getString(R.string.notification_channel_description) }
        context.getSystemService(NotificationManager::class.java)
            ?.run { createNotificationChannel(channel) }
    }

    companion object {
        /**
         * @return An instance of SimpleNotification
         */
        fun getInstance(context: Context) = SimpleNotification(context.applicationContext)

        const val CHANNEL_ID_GENERAL = "CHANNEL_ID_GENERAL"
        const val NOTIFICATION_ID_GENERAL = 1
    }
}
