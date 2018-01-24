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
    fun getShirtPantAtPos(id:Int): Observable<ShirtNPant>
    fun getShirtsAndPantsCount(): Observable<Int>
    fun getTypeCount(code:Int): Observable<Int>
    fun getFavsCount(): Observable<Int>
    fun getFavCombos(): Observable<List<FavCombo>>
    fun getFavComboAtPos(pos: Int): Observable<FavCombo>

    fun addFavCombos(favCombo: FavCombo): Observable<Boolean>
    fun addAShirt(shirt: ShirtNPant): Observable<Boolean>
    fun checkRandomCombo(randomCombo: RandomCombo): Single<RandomCombo>
    fun checkFavComboCount(favCombo: FavCombo): Observable<Int>
}