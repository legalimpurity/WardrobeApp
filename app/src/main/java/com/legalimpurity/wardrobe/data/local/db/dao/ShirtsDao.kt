package com.legalimpurity.wardrobe.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.legalimpurity.wardrobe.data.models.ShirtNPant

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Dao
interface ShirtsDao
{
    @Query("SELECT * FROM Shirts")
    fun loadAll(): List<ShirtNPant>

    @Query("SELECT count(*) FROM Shirts")
    fun getCount(): Int

    @Query("SELECT * FROM Shirts WHERE shirtOrPant = :code")
    fun loadByCode(code: Int?): List<ShirtNPant>

    @Insert()
    fun insert(shirt: ShirtNPant)
}