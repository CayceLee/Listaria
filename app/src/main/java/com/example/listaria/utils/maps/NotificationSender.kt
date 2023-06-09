package com.example.listaria.utils.maps

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.listaria.R
import com.example.listaria.model.ListOfListsData
import com.example.listaria.view.openedlist.CurrentOpenedListActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class NotificationSender(val context: Context) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun sendNotification(title: String, message: String, list: ListOfListsData) {
        val notificationId = 1
        val jsonString = Json.encodeToString(list)

        val intent = Intent(context, CurrentOpenedListActivity::class.java).apply {
            putExtra("list", jsonString)
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Create the notification channel (for Android 8.0 and higher)
        val channel = NotificationChannel(
            "channel_id",
            "Channel Name",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
        val notification = Notification.Builder(context, "channel_id")
        // Create the notification
//        val notification = NotificationCompat.Builder(context, "channel_id")
            .setSmallIcon(R.drawable.ic_trash)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Send the notification
        notificationManager.notify(notificationId, notification)
    }
}
