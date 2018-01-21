package com.legalimpurity.wardrobe.data.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.legalimpurity.wardrobe.data.local.db.dao.PantsDao
import com.legalimpurity.wardrobe.data.local.db.dao.ShirtsDao
import com.legalimpurity.wardrobe.data.models.Pant
import com.legalimpurity.wardrobe.data.models.Shirt

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Database(entities = [Pant::class, Shirt::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun PantsDao(): PantsDao
    abstract fun ShirtsDao(): ShirtsDao
}
