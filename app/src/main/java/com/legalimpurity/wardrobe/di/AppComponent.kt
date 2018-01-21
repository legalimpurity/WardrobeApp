package com.legalimpurity.wardrobe.di

import android.app.Application
import com.legalimpurity.wardrobe.WardropeApp
import com.legalimpurity.wardrobe.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by rajatkhanna on 20/01/18.
 */
@Singleton
@Component(modules = [AppModule::class, BuildersModule::class, AndroidSupportInjectionModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        fun application(application: Application) : Builder
        fun build() : AppComponent
    }

    fun inject(app: WardropeApp)
}