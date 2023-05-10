package com.ayoub.electricitybill.common

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ayoub.electricitybill.MainActivity
import com.ayoub.electricitybill.R

private const val NOTIFICATION_ID = 121


@SuppressLint("UnspecifiedImmutableFlag")
fun pushNotification(
    context: Context,
    channelId: String = "facture_id",
    channelName: String = "Facture",
    title: String,
    description: String? = null,
){
    val pendingIntent = PendingIntent.getActivity(
        context, 0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle(title)
        .setContentText(description)
        .setContentIntent(pendingIntent)

    if (Build.VERSION_CODES.O <= Build.VERSION.SDK_INT)
        notificationManager.createNotificationChannelIfNotExist(channelId, channelName)

    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
}

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationManager.createNotificationChannelIfNotExist(
    channelId: String,
    channelName: String,
    importance: Int = NotificationManager.IMPORTANCE_HIGH
) {
    var channel = this.getNotificationChannel(channelId)

    if (channel == null) {
        channel = NotificationChannel(
            channelId,
            channelName,
            importance
        )
        this.createNotificationChannel(channel)
    }
}