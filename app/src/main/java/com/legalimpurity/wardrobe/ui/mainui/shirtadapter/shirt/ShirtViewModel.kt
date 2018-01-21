package com.legalimpurity.wardrobe.ui.mainui.shirtadapter.shirt

import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProvider
import com.legalimpurity.wardrobe.ui.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by rajatkhanna on 21/01/18.
 */
class ShirtViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) : BaseViewModel<ShirtNavigator>(dataManager,schedulerProvider, compositeDisposable)
{

}