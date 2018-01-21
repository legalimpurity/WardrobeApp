package com.legalimpurity.wardrobe.data.local.db

import com.legalimpurity.wardrobe.data.models.Pant
import com.legalimpurity.wardrobe.data.models.Shirt
import io.reactivex.Observable

/**
 * Created by rajatkhanna on 20/01/18.
 */
interface DatabaseHelper
{
    fun getLocalShirts(): Observable<List<Shirt>>
    fun getLocalPants(): Observable<List<Pant>>

    fun addAPant(pant: Pant): Observable<Boolean>
    fun addAShirt(shirt: Shirt): Observable<Boolean>
}