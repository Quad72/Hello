package com.example.ch14_reciever

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //밑에서부터 알림 만드는 함수
        val manager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channelId="one-channel"
            val channelName="My Channel one"
            val channel= NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH
            )

            channel.description="My Channel One Description"
            channel.setShowBadge(true)
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes= AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            channel.enableLights(true)
            channel.lightColor= Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern= longArrayOf(100,200,100,200)

            manager.createNotificationChannel(channel)

            builder= NotificationCompat.Builder(context,channelId)
        }else{
            builder= NotificationCompat.Builder(context)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("홍길동")
        builder.setContentText("안녕하소")
        builder.setAutoCancel(true)
        builder.setOngoing(false)

        manager.notify(11,builder.build())
    }
}