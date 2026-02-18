package team.bue.bugle.feature.onboarding.viewmodel

import team.bue.bugle.core.ui.BaseViewModel

class OnboardingViewModel : BaseViewModel<OnboardingUiState, OnboardingSideEffect>(OnboardingUiState.Idle) {

    fun onKakaoLoginClick() {
        sendEffect(OnboardingSideEffect.NavigateToKakaoLogin)
    }

    fun onEmailLoginClick() {
        sendEffect(OnboardingSideEffect.NavigateToEmailLogin)
    }
}

sealed interface OnboardingUiState {
    data object Idle : OnboardingUiState
}

sealed interface OnboardingSideEffect {
    data object NavigateToKakaoLogin : OnboardingSideEffect
    data object NavigateToEmailLogin : OnboardingSideEffect
}