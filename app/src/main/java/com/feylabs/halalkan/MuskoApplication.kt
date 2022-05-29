package com.feylabs.halalkan

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.feylabs.halalkan.di.networkModule
import com.feylabs.halalkan.di.repositoryModule
import com.feylabs.halalkan.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MuskoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MuskoApplication)
            modules(listOf(
                networkModule,
                repositoryModule,
                viewModelModule
            ))
        }
    }
}