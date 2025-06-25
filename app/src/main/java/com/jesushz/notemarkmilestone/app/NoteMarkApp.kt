package com.jesushz.notemarkmilestone.app

import android.app.Application
import com.jesushz.notemarkmilestone.BuildConfig
import com.jesushz.notemarkmilestone.app.di.appModule
import com.jesushz.notemarkmilestone.auth.di.authModule
import com.jesushz.notemarkmilestone.core.database.di.databaseModule
import com.jesushz.notemarkmilestone.core.di.coreModule
import com.jesushz.notemarkmilestone.note.di.noteModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApp: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

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
                authModule,
                noteModule,
                databaseModule
            )
        }
    }

}