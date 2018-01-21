package com.legalimpurity.wardrobe

import android.app.Activity
import android.app.Application
import com.legalimpurity.wardrobe.di.AppComponent
import com.legalimpurity.wardrobe.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by rajatkhanna on 20/01/18.
 */
class WardropeApp : Application(), HasActivityInjector
{
    @Inject lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    // Initialized by Lambda the first time it is supposed to be used.
    val appComponent: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

}