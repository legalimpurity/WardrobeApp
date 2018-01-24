package com.legalimpurity.wardrobe.util.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.persistence.room.Room
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.legalimpurity.wardrobe.R
import com.legalimpurity.wardrobe.data.DataManagerImplementation
import com.legalimpurity.wardrobe.data.local.db.AppDatabase
import com.legalimpurity.wardrobe.data.local.db.DatabaseHelperImplementation
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelperImplementation
import com.legalimpurity.wardrobe.data.models.RandomCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProviderImplementation
import com.legalimpurity.wardrobe.ui.mainui.MainActivity
import com.legalimpurity.wardrobe.util.DB_NAME
import com.legalimpurity.wardrobe.util.PREF_NAME
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction


/**
 * Created by root on 22/1/18.
 */
class AlarmReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context?, p1: Intent?) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        context?.let {
            val stackBuilder = TaskStackBuilder.create(it)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(notificationIntent)

            val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(it)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(it.getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)

            val compositeDisposable = CompositeDisposable()
            val db = Room.databaseBuilder(it,
                    AppDatabase::class.java, DB_NAME).build()

            val dm = DataManagerImplementation(PreferenceHelperImplementation(it,PREF_NAME),DatabaseHelperImplementation(db))

            val sp = SchedulerProviderImplementation()
            compositeDisposable.add(dm
                    .canWeGiveShuffle()
                    .map { status ->
                        if(status)
                            dm.giveRandomComboNotGivenBefore().blockingFirst()
                        else
                        {
                            val rr =  RandomCombo()
                            rr.shirt_id = -1
                            rr
                        }

                    }
                    .subscribeOn(sp.io())
                    .map { randomCombo ->
                        if(randomCombo.shirt_id != -1) {
                            Observable.zip(dm.getShirtPantAtPos(randomCombo.pant_id),
                                    dm.getShirtPantAtPos(randomCombo.shirt_id),
                                    BiFunction<ShirtNPant, ShirtNPant, Array<Bitmap>> { shirt, pant ->
                                        arrayOf(Picasso.with(it).load(Uri.parse(shirt.shirtnPantPath)).get(),
                                                Picasso.with(context).load(Uri.parse(shirt.shirtnPantPath)).get())
                                    }).blockingFirst()
                        }
                        else
                            arrayOf(BitmapFactory.decodeResource(it.resources,
                                    R.drawable.ic_launcher_foreground),BitmapFactory.decodeResource(context.resources,
                                    R.drawable.ic_launcher_foreground))
                    }

                    .observeOn(sp.ui())
                    .subscribe({ bitmap: Array<Bitmap> ->
                        notificationBuilder.setLargeIcon(bitmap[0])
                        notificationBuilder.setContentText(it.getString(R.string.notifications_string))
                        val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.notify(0, notificationBuilder.build())
                    }))
        }


    }

}