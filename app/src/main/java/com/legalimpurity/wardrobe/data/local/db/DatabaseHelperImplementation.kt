package com.legalimpurity.wardrobe.data.local.db

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
    override fun getLocalShirts() = Observable.fromCallable<List<ShirtNPant>> { mAppDatabase.ShirtsDao().loadAll() }

    override fun addAShirt(shirt: ShirtNPant) = Observable.fromCallable {
        mAppDatabase.ShirtsDao().insert(shirt)
        true
    }


}