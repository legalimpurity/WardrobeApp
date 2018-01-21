package com.legalimpurity.wardrobe.data.local.db

import com.legalimpurity.wardrobe.data.models.Pant
import com.legalimpurity.wardrobe.data.models.Shirt
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Singleton
class DatabaseHelperImplementation @Inject constructor(private val mAppDatabase: AppDatabase) : DatabaseHelper
{
    override fun getLocalShirts() = Observable.fromCallable<List<Shirt>> { mAppDatabase.ShirtsDao().loadAll() }
    override fun getLocalPants() = Observable.fromCallable<List<Pant>> { mAppDatabase.PantsDao().loadAll() }

    override fun addAPant(pant: Pant) = Observable.fromCallable {
        mAppDatabase.PantsDao().insert(pant)
        true
    }

    override fun addAShirt(shirt: Shirt) = Observable.fromCallable {
        mAppDatabase.ShirtsDao().insert(shirt)
        true
    }


}