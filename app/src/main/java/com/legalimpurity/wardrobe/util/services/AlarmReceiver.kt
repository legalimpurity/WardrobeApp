package com.legalimpurity.wardrobe.util.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
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
class AlarmReceiver : BroadcastReceiver(), LifecycleOwner
{
    override fun getLifecycle(): Lifecycle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        val notificationIntent = Intent(context, MainActivity::class.java)

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context?.getString(R.string.app_name))
                .setContentText("asd")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val compositeDisposable = CompositeDisposable()
        val db = Room.databaseBuilder(context!!,
                AppDatabase::class.java, DB_NAME).build()

        val dm = DataManagerImplementation(PreferenceHelperImplementation(context,PREF_NAME),DatabaseHelperImplementation(db))

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
                                    arrayOf(Picasso.with(context).load(Uri.parse(shirt.shirtnPantPath)).get(),
                                            Picasso.with(context).load(Uri.parse(shirt.shirtnPantPath)).get())
                                }).blockingFirst()
                    }
                    else
                        arrayOf(BitmapFactory.decodeResource(context.resources,
                                R.drawable.ic_launcher_foreground),BitmapFactory.decodeResource(context.resources,
                                R.drawable.ic_launcher_foreground))
                }
                .observeOn(sp.ui())
                .subscribe({ bitmap: Array<Bitmap> ->
                        notificationBuilder.setLargeIcon(bitmap[0])
                        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.notify(0, notificationBuilder.build())
                }))

    }

}