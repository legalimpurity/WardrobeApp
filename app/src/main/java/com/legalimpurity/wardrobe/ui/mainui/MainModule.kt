package com.legalimpurity.wardrobe.ui.mainui

import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProvider
import com.legalimpurity.wardrobe.ui.mainui.shirtadapter.ShirtAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by rajatkhanna on 21/01/18.
 */
@Module
class MainModule
{
    @Provides
    fun provideMainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) = MainViewModel(dataManager, schedulerProvider, compositeDisposable)

    @Provides
    fun shirtAdapterProvider(main: MainActivity) : ShirtAdapter = ShirtAdapter(main.supportFragmentManager)
}