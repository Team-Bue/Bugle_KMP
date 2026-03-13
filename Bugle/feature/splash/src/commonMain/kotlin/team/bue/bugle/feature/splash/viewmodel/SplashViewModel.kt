package team.bue.bugle.feature.splash.viewmodel

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.bue.bugle.core.domain.usecase.auth.GetSavedTokenPairUseCase
import team.bue.bugle.core.ui.BaseViewModel

class SplashViewModel(
    private val getSavedTokenPairUseCase: GetSavedTokenPairUseCase,
) : BaseViewModel<SplashUiState, SplashSideEffect>(SplashUiState.Loading) {
    private val logger = Logger.withTag("SplashViewModel")

    init {
        viewModelScope.launch {
            delay(SPLASH_DURATION)
            runCatching {
                getSavedTokenPairUseCase()
            }.onSuccess { tokenPair ->
                if (tokenPair != null) {
                    sendEffect(SplashSideEffect.NavigateToHome)
                } else {
                    sendEffect(SplashSideEffect.NavigateToOnboarding)
                }
            }.onFailure { throwable ->
                logger.e(throwable) { "Failed to restore saved token. Navigating to onboarding." }
                sendEffect(SplashSideEffect.NavigateToOnboarding)
            }
        }
    }

    companion object {
        private const val SPLASH_DURATION = 1500L
    }
}

sealed interface SplashUiState {
    data object Loading : SplashUiState
}

sealed interface SplashSideEffect {
    data object NavigateToHome : SplashSideEffect
    data object NavigateToOnboarding : SplashSideEffect
}
