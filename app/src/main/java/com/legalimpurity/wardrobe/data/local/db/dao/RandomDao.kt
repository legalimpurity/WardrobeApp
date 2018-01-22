package com.legalimpurity.wardrobe.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.RandomCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant
import io.reactivex.Single

/**
 * Created by root on 22/1/18.
 */
@Dao
interface RandomDao
{
    @Query("SELECT * FROM Random where shirt_id = :shirtID and pant_id = :pantID")
    fun loadOne(shirtID: Int, pantID: Int): Single<RandomCombo>

    @Insert()
    fun insert(favCombo: RandomCombo)
}