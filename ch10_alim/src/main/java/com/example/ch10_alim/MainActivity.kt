package com.example.ch10_alim

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder:NotificationCompat.Builder

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channelId="one-channel"
            val channelName="My Channel one"
            val channel=NotificationChannel(
                channelId, channelName,NotificationManager.IMPORTANCE_HIGH
            )

            channel.description="My Channel One Description"
            channel.setShowBadge(true)
            val uri:Uri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes=AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            channel.enableLights(true)
            channel.lightColor= Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern= longArrayOf(100,200,100,200)

            manager.createNotificationChannel(channel)

            builder=NotificationCompat.Builder(this,channelId)
        }else{
            builder=NotificationCompat.Builder(this)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("Content Title")
        builder.setContentText("Content Massage")
        builder.setAutoCancel(true)
        builder.setOngoing(false)

        manager.notify(11,builder.build())

    }
}