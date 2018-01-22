package com.legalimpurity.wardrobe.data.local.db

import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.RandomCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by rajatkhanna on 20/01/18.
 */
interface DatabaseHelper
{
    fun getLocalShirts(code:Int): Observable<List<ShirtNPant>>
    fun getShirtsAndPantsCount(): Observable<Int>
    fun getFavCombos(): Observable<List<FavCombo>>
    fun addFavCombos(favCombo: FavCombo): Observable<Boolean>
    fun addAShirt(shirt: ShirtNPant): Observable<Boolean>
    fun checkRandomCombo(randomCombo: RandomCombo): Single<RandomCombo>
}