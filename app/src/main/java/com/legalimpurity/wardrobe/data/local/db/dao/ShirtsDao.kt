package com.legalimpurity.wardrobe.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.legalimpurity.wardrobe.data.models.Shirt

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Dao
interface ShirtsDao
{
    @Query("SELECT * FROM Shirts")
    fun loadAll(): List<Shirt>

    @Insert()
    fun insert(shirt: Shirt)
}