package com.legalimpurity.wardrobe.ui.mainui

import android.databinding.ObservableField
import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.data.models.Pant
import com.legalimpurity.wardrobe.data.models.Shirt
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

    // 0 = nothing
    // 1 = shirt
    // 2 = pant
    var shirtOrPantPic = 0
    var tempPicturePath = ""

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

    fun pictureAdded()
    {
        if(shirtOrPantPic == 1) {
            val pant = Pant()
            pant.pantPath = tempPicturePath
            getCompositeDisposable()?.add(getDataManager()
                    .addAPant(pant)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe({

                    }))
        }
        else if(shirtOrPantPic == 2) {
            var shirt = Shirt()
            shirt.shirtPath = tempPicturePath
            getCompositeDisposable()?.add(getDataManager()
                    .addAShirt(shirt)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe({

                    }))
        }
    }

    fun pictureCreated(pictureURI: String)
    {
        tempPicturePath = pictureURI
    }
}