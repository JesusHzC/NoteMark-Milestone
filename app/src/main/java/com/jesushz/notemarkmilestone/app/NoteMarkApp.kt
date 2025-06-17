package com.jesushz.notemarkmilestone.app

import android.app.Application
import com.jesushz.notemarkmilestone.BuildConfig
import com.jesushz.notemarkmilestone.app.di.appModule
import com.jesushz.notemarkmilestone.auth.di.authModule
import com.jesushz.notemarkmilestone.core.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@NoteMarkApp)
            modules(
                appModule,
                coreModule,
                authModule
            )
        }
    }

}