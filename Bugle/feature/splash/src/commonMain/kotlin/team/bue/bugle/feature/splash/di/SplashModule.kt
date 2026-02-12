package team.bue.bugle.feature.splash.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import team.bue.bugle.feature.splash.viewmodel.SplashViewModel

val splashModule = module {
    viewModelOf(::SplashViewModel)
}
