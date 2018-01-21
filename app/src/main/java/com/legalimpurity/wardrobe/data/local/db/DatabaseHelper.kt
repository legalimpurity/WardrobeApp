package com.legalimpurity.wardrobe.data.local.db

import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import io.reactivex.Observable

/**
 * Created by rajatkhanna on 20/01/18.
 */
interface DatabaseHelper
{
    fun getLocalShirts(code:Int): Observable<List<ShirtNPant>>
    fun getFavCombos(): Observable<List<FavCombo>>
    fun addFavCombos(favCombo: FavCombo): Observable<Boolean>
    fun addAShirt(shirt: ShirtNPant): Observable<Boolean>
}