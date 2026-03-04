package team.bue.bugle

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import team.bue.bugle.di.appModule

class BugleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BugleApplication)
            properties(mapOf("BUGLE_BASE_URL" to BuildConfig.BUGLE_BASE_URL))
            modules(appModule)
        }
    }
}
