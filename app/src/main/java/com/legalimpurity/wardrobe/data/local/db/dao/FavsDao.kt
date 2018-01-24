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

    @Query("SELECT * FROM Favs WHERE id = :pos limit 1")
    fun loadByPos(pos: Int?): FavCombo

    @Insert()
    fun insert(favCombo: FavCombo)

    @Query("SELECT count(*) FROM Favs where shirt_id = :shirtID and pant_id = :pantID")
    fun checkCombo(shirtID: Int, pantID: Int): Int

}