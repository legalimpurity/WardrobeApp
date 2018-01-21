package com.legalimpurity.wardrobe.di

import com.legalimpurity.wardrobe.ui.mainui.MainActivity
import com.legalimpurity.wardrobe.ui.mainui.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Module
abstract class BuildersModule
{
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun bindSplashActivity() : MainActivity
}