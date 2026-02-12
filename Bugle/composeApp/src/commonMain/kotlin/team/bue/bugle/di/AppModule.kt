package team.bue.bugle.di

import org.koin.dsl.module
import team.bue.bugle.feature.splash.di.splashModule

val appModule = module {
    includes(splashModule)
}