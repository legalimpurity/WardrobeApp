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
    override fun getLastShirtSelected() = preferencesHelper.getLastShirtSelected()
    override fun setLastShirtSelected(pos: Int) = preferencesHelper.setLastShirtSelected(pos)

    override fun getLastPantSelected() = preferencesHelper.getLastPantSelected()
    override fun setLastPantSelected(pos: Int) = preferencesHelper.setLastPantSelected(pos)

    override fun getLocalShirts(code: Int): Observable<List<ShirtNPant>> = databaseHelper.getLocalShirts(code)
    override fun addAShirt(shirt: ShirtNPant): Observable<Boolean> = databaseHelper.addAShirt(shirt)
}