package com.legalimpurity.wardrobe.data

import com.legalimpurity.wardrobe.data.local.db.DatabaseHelper
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelper
import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.RandomCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.*
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


    override fun checkFavComboExist(favCombo: FavCombo) = databaseHelper.checkFavComboCount(favCombo)
            .map { count ->
                count != 0
            }

    // Not given not given before.
    override fun giveRandomComboNotGivenBefore() = Observable.zip(
            databaseHelper.getTypeCount(1),
            databaseHelper.getTypeCount(2),
            BiFunction { shirtsCount:Int, pantsCount:Int ->
                val totalCount = shirtsCount+pantsCount
                val returner = RandomCombo()
                val maxNumberOfCombinations = totalCount * (totalCount-1)/2
                val rnd = Random()
                val shirtRandPos = rnd.nextInt(shirtsCount - 1) + 1
                val pantRandPos = rnd.nextInt(pantsCount - 1) + 1
                returner.pant_id = pantRandPos
                returner.shirt_id = shirtRandPos
                returner
            }
    )

    // Section to check recursively if the suffle arragement has been suggested before or not, was not able to implement cause studio was identifying recursive calling.
//            .map { randomCombo ->
//                Observable.zip(
//                        checkRandomComboNotExist(randomCombo),
//                        Observable.just<RandomCombo>(randomCombo),
//                        BiFunction { existNotExist:Boolean, randomCombo2:RandomCombo ->
//                            if(existNotExist)
//                                randomCombo2
//                            else
//                                giveRandomComboNotGivenBefore()
//                        }
//                )
//            }


    override fun giveRandomFavCombo() = databaseHelper.getFavsCount()
            .map { countt ->
                val rnd = Random()
                rnd.nextInt(countt)
            }
            .map { randomNumberPos ->
                getFavCombos().blockingFirst()[randomNumberPos]
            }

    // Minimum one shirt and one pant should be there, before we try to shuffle.
    override fun canWeGiveShuffle() = Observable.zip(
            databaseHelper.getTypeCount(1)
                    .map { count ->
                        count != 0
                    },
            databaseHelper.getTypeCount(2)
                    .map { count ->
                        count != 0
                    },
            BiFunction { shirtsAreThere: Boolean, pantsAreThere: Boolean ->
                shirtsAreThere && pantsAreThere
            }
    )

    override fun getLastShirtSelected() = preferencesHelper.getLastShirtSelected()
    override fun setLastShirtSelected(pos: Int) = preferencesHelper.setLastShirtSelected(pos)

    override fun getLastPantSelected() = preferencesHelper.getLastPantSelected()
    override fun setLastPantSelected(pos: Int) = preferencesHelper.setLastPantSelected(pos)

    override fun getLocalShirts(code: Int) = databaseHelper.getLocalShirts(code)
    override fun getShirtPantAtPos(pos: Int) = databaseHelper.getShirtPantAtPos(pos)
    override fun getShirtsAndPantsCount() = databaseHelper.getShirtsAndPantsCount()
    override fun getFavsCount() = databaseHelper.getFavsCount()
    override fun getTypeCount(code: Int) = databaseHelper.getTypeCount(code)
    override fun getFavCombos() = databaseHelper.getFavCombos()
    override fun checkRandomCombo(randomCombo: RandomCombo) = databaseHelper.checkRandomCombo(randomCombo)
    override fun checkFavComboCount(favCombo: FavCombo) = databaseHelper.checkFavComboCount(favCombo)
    override fun addFavCombos(favCombo: FavCombo) = databaseHelper.addFavCombos(favCombo)
    override fun addAShirt(shirt: ShirtNPant): Observable<Boolean> = databaseHelper.addAShirt(shirt)

    override fun removeFavCombos(favCombo: FavCombo) = databaseHelper.removeFavCombos(favCombo)

}