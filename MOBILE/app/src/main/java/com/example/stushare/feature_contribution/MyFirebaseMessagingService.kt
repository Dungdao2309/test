package com.example.stushare.feature_contribution

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.stushare.R

// 2. Import đúng các class trong dự án của bạn (com.example.stushare...)
import com.example.stushare.R
import com.example.stushare.feature_contribution.db.AppDatabase
import com.example.stushare.feature_contribution.db.NotificationEntity
import com.example.stushare.feature_contribution.ui.noti.NotificationItem

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var title: String? = null
        var body: String? = null

        if (remoteMessage.data.isNotEmpty()) {
            title = remoteMessage.data["title"]
            body = remoteMessage.data["body"]
        }
        else if (remoteMessage.notification != null) {
            title = remoteMessage.notification!!.title
            body = remoteMessage.notification!!.body
        }

        if (title == null || body == null) {
            return
        }

        // Tạo Entity
        val newNotification = NotificationEntity(
            title = title,
            message = body,
            timestamp = System.currentTimeMillis(),
            type = NotificationItem.Type.INFO.name,
            isRead = false
        )

        // Lưu vào Room
        val notificationDao = AppDatabase.getInstance(applicationContext).notificationDao()
        GlobalScope.launch {
            notificationDao.addNotification(newNotification)
        }

        // Hiển thị thông báo
        sendSystemNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MyFCMToken", "FCM Registration Token: $token")
    }

    private fun sendSystemNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "stushare_channel_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Thông báo chung",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notifications) // Đảm bảo icon này tồn tại
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}