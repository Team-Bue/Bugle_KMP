package team.bue.bugle.feature.emaillogin.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import team.bue.bugle.feature.emaillogin.viewmodel.EmailLoginViewModel

val emailLoginModule = module {
    viewModelOf(::EmailLoginViewModel)
}
