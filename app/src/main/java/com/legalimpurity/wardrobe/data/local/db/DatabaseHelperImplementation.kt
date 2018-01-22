package com.legalimpurity.wardrobe.data.local.db

import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.RandomCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Singleton
class DatabaseHelperImplementation @Inject constructor(private val mAppDatabase: AppDatabase) : DatabaseHelper
{
    override fun getFavCombos() = Observable.fromCallable<List<FavCombo>> { mAppDatabase.FavsComboDao().loadAll() }

    override fun checkRandomCombo(randomCombo: RandomCombo) = mAppDatabase.RandomDao().loadOne(randomCombo.shirt_id,randomCombo.pant_id)

    override fun addFavCombos(favCombo: FavCombo) = Observable.fromCallable {
        mAppDatabase.FavsComboDao().insert(favCombo)
        true
    }

    override fun getLocalShirts(code: Int) = Observable.fromCallable<List<ShirtNPant>> { mAppDatabase.ShirtsDao().loadByCode(code) }
    override fun getShirtsAndPantsCount() = Observable.fromCallable<Int> { mAppDatabase.ShirtsDao().getCount() }

    override fun addAShirt(shirt: ShirtNPant) = Observable.fromCallable {
        mAppDatabase.ShirtsDao().insert(shirt)
        true
    }

}