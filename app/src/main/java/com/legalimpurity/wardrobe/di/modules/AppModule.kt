package com.legalimpurity.wardrobe.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.legalimpurity.wardrobe.data.DataManager
import com.legalimpurity.wardrobe.data.DataManagerImplementation
import com.legalimpurity.wardrobe.data.local.db.AppDatabase
import com.legalimpurity.wardrobe.data.local.db.DatabaseHelper
import com.legalimpurity.wardrobe.data.local.db.DatabaseHelperImplementation
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelper
import com.legalimpurity.wardrobe.data.local.prefs.PreferenceHelperImplementation
import com.legalimpurity.wardrobe.di.DatabaseInfo
import com.legalimpurity.wardrobe.di.PreferenceInfo
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProvider
import com.legalimpurity.wardrobe.di.modules.appmoduleprovides.rx.SchedulerProviderImplementation
import com.legalimpurity.wardrobe.util.DB_NAME
import com.legalimpurity.wardrobe.util.PREF_NAME
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideDataManager(appDataManager: DataManagerImplementation): DataManager = appDataManager

    @Singleton
    @Provides
    fun provideScheduleProvider(): SchedulerProvider = SchedulerProviderImplementation()

    @Singleton
    @Provides
    fun provideContext(app: Application): Context = app

    @Singleton
    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

    @Singleton
    @Provides
    fun providePreferenceHelper(appPreferencesHelper: PreferenceHelperImplementation): PreferenceHelper = appPreferencesHelper

    @Provides
    @Singleton
    fun provideAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, dbName).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideDbHelper(appDbHelper: DatabaseHelperImplementation): DatabaseHelper {
        return appDbHelper
    }

    @PreferenceInfo
    @Provides
    fun providePreferenceName(): String = PREF_NAME

    @DatabaseInfo
    @Provides
    fun provideDatabaseName(): String = DB_NAME
}