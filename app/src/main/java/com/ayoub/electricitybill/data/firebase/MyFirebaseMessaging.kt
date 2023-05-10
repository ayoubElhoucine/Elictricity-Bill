package com.ayoub.electricitybill.data.firebase

import android.annotation.SuppressLint
import com.ayoub.electricitybill.common.pushNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessaging: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.title?.let { title ->
            pushNotification(
                context = applicationContext,
                title = title,
                description = message.notification?.body,
            )
        }
    }

}