package com.legalimpurity.wardrobe.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by root on 22/1/18.
 */
@Entity(tableName = "Favs")
class FavCombo
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var shirt_id: Int = 0
    var pant_id: Int = 0

}