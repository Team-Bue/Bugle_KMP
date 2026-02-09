package team.bue.bugle

import org.koin.core.context.startKoin
import team.bue.bugle.di.appModule

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
