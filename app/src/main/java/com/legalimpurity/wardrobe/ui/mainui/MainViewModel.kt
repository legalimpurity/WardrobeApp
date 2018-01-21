package com.legalimpurity.wardrobe.ui.mainui

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProvider
import com.legalimpurity.wardrobe.ui.base.BaseViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by rajatkhanna on 21/01/18.
 */
class MainViewModel(dataManager: DataManager, schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable) : BaseViewModel<MainNavigator>(dataManager,schedulerProvider, compositeDisposable)
{

    val shirtDataObservableArrayList = ObservableArrayList<ShirtNPant>()
    val pantsDataObservableArrayList = ObservableArrayList<ShirtNPant>()

    val shirtsLiveData: MutableLiveData<List<ShirtNPant>> = MutableLiveData()
    val pantsLiveData: MutableLiveData<List<ShirtNPant>> = MutableLiveData()

    val shirtBeingViewedPosition: MutableLiveData<Int> = MutableLiveData()
    val pantBeingViewedPosition: MutableLiveData<Int> = MutableLiveData()

    val shirt_available = ObservableField<String>()
    val pant_available = ObservableField<String>()

    // 0 = nothing
    // 1 = shirt
    // 2 = pant
    var shirtOrPantPic = 0
    var tempPicturePath = ""

    init {
        fetchShirtsListMix()
        fetchPantsListMix()
        fetchPositions()
    }

    fun addShirtsToList(shirtsData: List<ShirtNPant>) {
        shirtDataObservableArrayList.clear()
        shirtDataObservableArrayList.addAll(shirtsData)
    }

    fun addPantsToList(pantsData: List<ShirtNPant>) {
        pantsDataObservableArrayList.clear()
        pantsDataObservableArrayList.addAll(pantsData)
    }

    fun fetchPositions()
    {
        shirtBeingViewedPosition.value = getDataManager().getLastShirtSelected()
        pantBeingViewedPosition.value = getDataManager().getLastPantSelected()
    }

    fun fetchShirtsListMix() {
        getCompositeDisposable()?.add(getDataManager()
                .getLocalShirts(1)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe({ shirtsDataResponse ->
                    shirtsLiveData.value = shirtsDataResponse
                }, { throwable ->
                    getNavigator()?.apiError(throwable)
                }))
    }

    fun fetchPantsListMix() {
        getCompositeDisposable()?.add(getDataManager()
                .getLocalShirts(2)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe({ pantsDataResponse ->
                    pantsLiveData.value = pantsDataResponse
                }, { throwable ->
                    getNavigator()?.apiError(throwable)
                }))
    }


    fun addPantPicture()
    {
        shirtOrPantPic = 2
        getNavigator()?.openAddder()
    }

    fun addShirtPicture()
    {
        shirtOrPantPic = 1
        getNavigator()?.openAddder()
    }

    fun pictureAdded() {
        val shirtNPant = ShirtNPant()
        shirtNPant.shirtnPantPath = tempPicturePath
        shirtNPant.shirtOrPant = shirtOrPantPic
        getCompositeDisposable()?.add(getDataManager()
                .addAShirt(shirtNPant)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe({
                    if(shirtOrPantPic == 1)
                        fetchShirtsListMix()
                    else(shirtOrPantPic == 2)
                        fetchPantsListMix()
                    getNavigator()?.updateAdapter(shirtOrPantPic)
                }))
    }

    fun pictureCreated(pictureURI: String)
    {
        tempPicturePath = pictureURI
    }

    // We can rely on positions cause there is no delete functionality in the app.
    fun setPantPosBeingSeen(pos:Int)
    {
        getDataManager().setLastPantSelected(pos)
        shirtBeingViewedPosition.value = pos
    }

    fun setShirtPosBeingSeen(pos:Int)
    {
        getDataManager().setLastShirtSelected(pos)
        pantBeingViewedPosition.value = pos
    }
}