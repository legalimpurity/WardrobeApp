package com.legalimpurity.wardrobe.ui.mainui

import android.databinding.ObservableField
import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProvider
import com.legalimpurity.wardrobe.ui.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by rajatkhanna on 21/01/18.
 */
class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) : BaseViewModel<MainNavigator>(dataManager,schedulerProvider, compositeDisposable)
{
    val shirt_available = ObservableField<String>()
    val pant_available = ObservableField<String>()
}