package team.bue.bugle.di

import org.koin.dsl.module
import team.bue.bugle.core.data.di.coreApiModule
import team.bue.bugle.feature.emaillogin.di.emailLoginModule
import team.bue.bugle.feature.findpassword.di.findPasswordModule
import team.bue.bugle.feature.onboarding.di.onboardingModule
import team.bue.bugle.feature.signup.di.signUpModule
import team.bue.bugle.feature.splash.di.splashModule

val appModule =
    module {
        includes(
            splashModule,
            onboardingModule,
            emailLoginModule,
            findPasswordModule,
            signUpModule,
            coreApiModule,
        )
    }
