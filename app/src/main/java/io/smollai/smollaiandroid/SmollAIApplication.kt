package io.smollai.smollaiandroid

import android.app.Application
import io.smollai.smollaiandroid.data.ObjectBoxStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class SmollAIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SmollAIApplication)
            modules(KoinAppModule().module)
        }
        ObjectBoxStore.init(this)
    }
}
