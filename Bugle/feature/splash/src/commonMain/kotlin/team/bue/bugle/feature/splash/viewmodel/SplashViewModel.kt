package team.bue.bugle.feature.splash.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.bue.bugle.core.ui.BaseViewModel

class SplashViewModel : BaseViewModel<SplashUiState, SplashSideEffect>(SplashUiState.Loading) {

    init {
        viewModelScope.launch {
            delay(SPLASH_DURATION)
            sendEffect(SplashSideEffect.NavigateToMain)
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
    data object NavigateToMain : SplashSideEffect
}
