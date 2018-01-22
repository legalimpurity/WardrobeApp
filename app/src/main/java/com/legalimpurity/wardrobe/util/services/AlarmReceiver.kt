package com.legalimpurity.wardrobe.util.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import com.legalimpurity.wardrobe.R.mipmap.ic_launcher
import android.app.PendingIntent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.legalimpurity.wardrobe.R
import com.legalimpurity.wardrobe.ui.mainui.MainActivity


/**
 * Created by root on 22/1/18.
 */
class AlarmReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context?, p1: Intent?) {
        val notificationIntent = Intent(context, MainActivity::class.java)

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context)

        val notification = builder.setContentTitle("Demo App Notification")
                .setContentText("New Notification From Demo App..")
                .setTicker("New Message Alert!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).build()

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)

    }

}