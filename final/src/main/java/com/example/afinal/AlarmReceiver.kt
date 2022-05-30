package com.example.afinal

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

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
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
        val bundle=intent.getBundleExtra("todo")
        val todo=bundle?.getString("todo")
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("할일")
        builder.setContentText("$todo")
        builder.setAutoCancel(true)
        builder.setOngoing(false)

        manager.notify(11,builder.build())
    }
}