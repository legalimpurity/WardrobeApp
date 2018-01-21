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
//    private val pantsDataObservableArrayList = ObservableArrayList<ShirtNPant>()

    val shirtsLiveData: MutableLiveData<List<ShirtNPant>> = MutableLiveData()
//    private val pantsLiveData: MutableLiveData<List<ShirtNPant>> = MutableLiveData()

    val shirt_available = ObservableField<String>()
    val pant_available = ObservableField<String>()

    // 0 = nothing
    // 1 = shirt
    // 2 = pant
    var shirtOrPantPic = 0
    var tempPicturePath = ""

    fun addShirtsToList(timeTableData: List<ShirtNPant>) {
        shirtDataObservableArrayList.clear()
        shirtDataObservableArrayList.addAll(timeTableData)
    }

    fun fetchShirtsListMix() {
        getCompositeDisposable()?.add(getDataManager()
                .getLocalShirts()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe({ timeTableDataResponse ->
                    shirtsLiveData.value = timeTableDataResponse
                    shirt_available.set("")
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

                }))
    }

    fun pictureCreated(pictureURI: String)
    {
        tempPicturePath = pictureURI
    }
}