package com.legalimpurity.wardrobe.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.legalimpurity.wardrobe.data.models.FavCombo

/**
 * Created by root on 22/1/18.
 */
@Dao
interface FavsDao
{
    @Query("SELECT * FROM Favs")
    fun loadAll(): List<FavCombo>

    @Query("SELECT count(*) FROM Favs")
    fun getCount(): Int

    @Insert()
    fun insert(favCombo: FavCombo)

    @Query("delete from Favs WHERE shirt_id = :shirt_id and pant_id = :pant_id")
    fun delete(shirt_id: Int,pant_id:Int)

    @Query("SELECT count(*) FROM Favs where shirt_id = :shirtID and pant_id = :pantID")
    fun checkCombo(shirtID: Int, pantID: Int): Int
}