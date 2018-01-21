package com.legalimpurity.wardrobe.data

import com.legalimpurity.wardrobe.data.local.db.DatabaseHelper
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelper
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by rajatkhanna on 20/01/18.
 */
class DataManagerImplementation @Inject constructor(val preferencesHelper: PreferenceHelper, val databaseHelper: DatabaseHelper): DataManager
{
    override fun getLocalShirts(code: Int): Observable<List<ShirtNPant>> = databaseHelper.getLocalShirts(code)
    override fun addAShirt(shirt: ShirtNPant): Observable<Boolean> = databaseHelper.addAShirt(shirt)
}