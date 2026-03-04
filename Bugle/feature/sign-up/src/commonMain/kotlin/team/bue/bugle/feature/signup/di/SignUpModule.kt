package team.bue.bugle.feature.signup.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import team.bue.bugle.feature.signup.viewmodel.SignUpViewModel

val signUpModule = module {
    viewModelOf(::SignUpViewModel)
}

