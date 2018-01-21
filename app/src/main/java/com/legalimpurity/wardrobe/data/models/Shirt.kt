package com.legalimpurity.wardrobe.data.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Entity(tableName = "Shirts")
class Shirt
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var shirtPath: String = ""
}