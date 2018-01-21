package com.legalimpurity.wardrobe.ui.mainui.shirtadapter.shirt

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by rajatkhanna on 21/01/18.
 */
@Module
abstract class ShirtFragmentProvider
{
    @ContributesAndroidInjector(modules = [ShirtFragmentModule::class])
    abstract fun provideEventsFragmentFactory():Shirtfragment
}