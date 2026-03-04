package team.bue.bugle.feature.findpassword.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import team.bue.bugle.feature.findpassword.viewmodel.FindPasswordViewModel

val findPasswordModule = module {
    viewModelOf(::FindPasswordViewModel)
}
