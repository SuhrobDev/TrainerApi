package com.example.newtrainerapp.di.component

import android.app.Application
import com.example.newtrainerapp.app.App
import com.example.newtrainerapp.di.module.ActivityBuildersModule
import com.example.newtrainerapp.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Microstar on 02.06.2020.
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuildersModule::class
//        ActivityBuildersModule::class,
//        ViewModelFactoryModule::class,
    ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}
