package com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx

import io.reactivex.Scheduler

/**
 * Created by rajatkhanna on 20/01/18.
 */
interface SchedulerProvider
{
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun io(): Scheduler
}