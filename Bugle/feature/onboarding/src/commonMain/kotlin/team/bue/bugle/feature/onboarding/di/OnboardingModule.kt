package team.bue.bugle.feature.onboarding.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import team.bue.bugle.feature.onboarding.viewmodel.OnboardingViewModel

val onboardingModule = module {
    viewModelOf(::OnboardingViewModel)
}