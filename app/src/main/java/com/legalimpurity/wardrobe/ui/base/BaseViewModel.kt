package com.legalimpurity.wardrobe.ui.base

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableBoolean
import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by rajatkhanna on 21/01/18.
 */
abstract class BaseViewModel<N : BaseNavigator>(dataManager: DataManager, schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) : ViewModel()
{
    private var mNavigator: N? = null

    private var compositeDisposable: CompositeDisposable? = null
    private var schedulerProvider: SchedulerProvider? = null
    private var dataManager: DataManager? = null
    private val mIsLoading = ObservableBoolean(false)

    init {
        this.dataManager = dataManager
        this.schedulerProvider = schedulerProvider
        this.compositeDisposable = compositeDisposable
    }

    fun getCompositeDisposable() = compositeDisposable
    fun getSchedulerProvider() = schedulerProvider!!
    fun getDataManager() = dataManager!!

    fun setNavigator(navigator: N) {
        this.mNavigator = navigator
    }

    fun getNavigator(): N? = mNavigator

    fun getIsLoading(): ObservableBoolean {
        return mIsLoading
    }

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading.set(isLoading)
    }
}