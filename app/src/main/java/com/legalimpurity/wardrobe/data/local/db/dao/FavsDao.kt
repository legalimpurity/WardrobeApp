package com.legalimpurity.wardrobe.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant

/**
 * Created by root on 22/1/18.
 */
@Dao
interface FavsDao
{
    @Query("SELECT * FROM Favs")
    fun loadAll(): List<FavCombo>

    @Insert()
    fun insert(favCombo: FavCombo)
}