package team.bue.bugle.feature.findid.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import team.bue.bugle.feature.findid.viewmodel.FindIdViewModel

val findIdModule = module {
    viewModelOf(::FindIdViewModel)
}
