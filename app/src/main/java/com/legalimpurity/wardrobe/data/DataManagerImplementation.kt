package com.legalimpurity.wardrobe.data

import com.legalimpurity.wardrobe.data.local.db.DatabaseHelper
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelper
import com.legalimpurity.wardrobe.data.models.Pant
import com.legalimpurity.wardrobe.data.models.Shirt
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by rajatkhanna on 20/01/18.
 */
class DataManagerImplementation @Inject constructor(val preferencesHelper: PreferenceHelper, val databaseHelper: DatabaseHelper): DataManager
{
    override fun getLocalShirts(): Observable<List<Shirt>> = databaseHelper.getLocalShirts()
    override fun getLocalPants(): Observable<List<Pant>> = databaseHelper.getLocalPants()

    override fun addAPant(pant: Pant): Observable<Boolean> = databaseHelper.addAPant(pant)
    override fun addAShirt(shirt: Shirt): Observable<Boolean> = databaseHelper.addAShirt(shirt)
}