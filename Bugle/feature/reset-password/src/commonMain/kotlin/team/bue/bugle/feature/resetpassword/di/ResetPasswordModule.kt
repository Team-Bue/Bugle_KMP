package team.bue.bugle.feature.resetpassword.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import team.bue.bugle.feature.resetpassword.viewmodel.ResetPasswordViewModel

val resetPasswordModule = module {
    viewModelOf(::ResetPasswordViewModel)
}
