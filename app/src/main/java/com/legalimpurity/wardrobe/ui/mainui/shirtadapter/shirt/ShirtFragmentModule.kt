package com.legalimpurity.wardrobe.ui.mainui.shirtadapter.shirt

import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by rajatkhanna on 21/01/18.
 */
@Module
class ShirtFragmentModule
{
    @Provides
    fun providesShirtViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) = ShirtViewModel(dataManager,schedulerProvider,compositeDisposable)
}