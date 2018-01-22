package com.legalimpurity.wardrobe.data

import com.legalimpurity.wardrobe.data.local.db.DatabaseHelper
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelper
import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.RandomCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import io.reactivex.Observable
import java.util.Random
import java.util.function.BiFunction
import javax.inject.Inject

/**
 * Created by rajatkhanna on 20/01/18.
 */
class DataManagerImplementation @Inject constructor(val preferencesHelper: PreferenceHelper, val databaseHelper: DatabaseHelper): DataManager
{
    override fun checkRandomComboNotExist(randomCombo: RandomCombo) = databaseHelper.checkRandomCombo(randomCombo)
            .map {
                false
            }
            .doOnError({
                if(it.message == "")
                    true
            })
            .toObservable()

    override fun giveRandomComboNotGivenBefore(): Observable<RandomCombo> = databaseHelper.getShirtsAndPantsCount()
            .map { countt ->
                val returner = RandomCombo()
                val maxNumberOfCombinations = countt * (countt-1)/2
                val rnd = Random()
                val rand1 = rnd.nextInt(countt)
                val rand2 = rnd.nextInt(countt)
                returner.pant_id = rand1
                returner.shirt_id = rand2
                returner
            }
//            .map { randomCombo ->
//                Observable.zip(
//                        checkRandomComboNotExist(randomCombo),
//                        Observable.just<RandomCombo>(randomCombo),
//                        BiFunction { existNotExist:Boolean, randomCombo2:RandomCombo ->
//                            randomCombo2
//                        }
//                )
//            }
//            .toObservable()


    override fun getLastShirtSelected() = preferencesHelper.getLastShirtSelected()
    override fun setLastShirtSelected(pos: Int) = preferencesHelper.setLastShirtSelected(pos)

    override fun getLastPantSelected() = preferencesHelper.getLastPantSelected()
    override fun setLastPantSelected(pos: Int) = preferencesHelper.setLastPantSelected(pos)

    override fun getLocalShirts(code: Int) = databaseHelper.getLocalShirts(code)
    override fun getShirtsAndPantsCount() = databaseHelper.getShirtsAndPantsCount()
    override fun getFavCombos() = databaseHelper.getFavCombos()
    override fun checkRandomCombo(randomCombo: RandomCombo) = databaseHelper.checkRandomCombo(randomCombo)
    override fun addFavCombos(favCombo: FavCombo) = databaseHelper.addFavCombos(favCombo)
    override fun addAShirt(shirt: ShirtNPant): Observable<Boolean> = databaseHelper.addAShirt(shirt)


}