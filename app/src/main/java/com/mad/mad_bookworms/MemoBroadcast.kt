package com.mad.mad_bookworms

import androidx.core.app.NotificationManagerCompat

import android.R

import android.graphics.BitmapFactory

import android.graphics.Bitmap



import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat


class MemoBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val repeating_Intent = Intent(context, MainActivity::class.java)
        repeating_Intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            repeating_Intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "Notification")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.btn_star)
            .setLargeIcon(
                Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.btn_star_big_on
                    ), 128, 128, false
                )
            )
            .setContentTitle("BookWorm")
            .setContentText("Do not miss out your daily check in reward!")
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(200, builder.build())
    }
}