package com.legalimpurity.wardrobe.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.legalimpurity.wardrobe.data.models.Pant

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Dao
interface PantsDao
{
    @Query("SELECT * FROM Pants")
    fun loadAll(): List<Pant>

    @Insert()
    fun insert(pant: Pant)
}