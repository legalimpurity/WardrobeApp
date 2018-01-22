package com.legalimpurity.wardrobe.data

import com.legalimpurity.wardrobe.data.local.db.DatabaseHelper
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelper
import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.RandomCombo
import io.reactivex.Observable

/**
 * Created by rajatkhanna on 20/01/18.
 */
interface DataManager : PreferenceHelper, DatabaseHelper
{
    fun checkRandomComboNotExist(randomCombo: RandomCombo) : Observable<Boolean>
    fun giveRandomComboNotGivenBefore() : Observable<RandomCombo>
    fun giveRandomFavCombo() : Observable<FavCombo>
    fun canWeGiveShuffle() : Observable<Boolean>
}