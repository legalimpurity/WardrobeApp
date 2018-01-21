package com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by rajatkhanna on 20/01/18.
 */
class SchedulerProviderImplementation : SchedulerProvider
{
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun io(): Scheduler = Schedulers.io()
}