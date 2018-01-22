package com.legalimpurity.wardrobe.data.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.legalimpurity.wardrobe.data.local.db.dao.FavsDao
import com.legalimpurity.wardrobe.data.local.db.dao.RandomDao
import com.legalimpurity.wardrobe.data.local.db.dao.ShirtsDao
import com.legalimpurity.wardrobe.data.models.FavCombo
import com.legalimpurity.wardrobe.data.models.RandomCombo
import com.legalimpurity.wardrobe.data.models.ShirtNPant

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Database(entities = [ShirtNPant::class, FavCombo::class, RandomCombo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ShirtsDao(): ShirtsDao
    abstract fun FavsComboDao(): FavsDao
    abstract fun RandomDao(): RandomDao
}